package org.jaggy.bukkit.ample.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class SQLITE extends DB {
	
	private File sqlFile;

	public SQLITE(Logger log, String dbHost, String dbName, String prefix) {
		super(log, dbHost, dbName, prefix);
		this.log = log;
		this.dbHost = dbHost;
		this.dbName = dbName;
		this.PREFIX = prefix;
		File folder = new File(this.dbHost);
		if (!folder.exists()) {
			folder.mkdir();
		}
		sqlFile = new File(folder.getAbsolutePath() +"/"+ prefix + dbName);
		if (!sqlFile.exists()) {
			try {
				sqlFile.createNewFile();
			} catch (IOException e) {
				this.Error("SQL exception in constructor: " + e);
			}
		}
	}
	
	/**
	 * Checks to see if the table exists and creates the table
	 * in the SQLite database. 
	 */
	@Override
	public void createTables() {
		if(this.checkTable(this.PREFIX+"Responses") == false) {
		 ResultSet result = this.query("CREATE TABLE \""+this.PREFIX+
				"Responses\" (\"id\" integer NOT NULL ON CONFLICT FAIL PRIMARY KEY AUTOINCREMENT,"+
				 "\"keyphrase\" text(200,0), \"response\" text(200,0));");
		try {
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.Error("DB error: "+e);
		}
		}
	}
	
	@Override
	public Connection open() {
		if (initialize()) {
			try {
			  this.connection = DriverManager.getConnection("jdbc:sqlite:" +
					  	   sqlFile.getAbsolutePath());
			  return this.connection;
			} catch (SQLException e) {
			  this.Error("SQL exception in open(): " + e);
			}
		}
		return null;
	}
	@Override
	public  ResultSet query(String query) {
		Statement statement = null;
		ResultSet result = null;
		try {
			connection = this.getConnection();
			statement = connection.createStatement();
			switch (this.getStatement(query)) {
				case SELECT:
					result = statement.executeQuery(query);
					break;
				
			    case INSERT:
			    case UPDATE:
			    case DELETE:	
			    case CREATE:
			    case ALTER:
			    case DROP:
			    case TRUNCATE:
			    case RENAME:
			    case DO:
			    case REPLACE:
			    case LOAD:
			    case HANDLER:
			    case CALL:
			    	this.lastUpdate = statement.executeUpdate(query);
			    	break;
				
				default:
					result = statement.executeQuery(query);
			}
			return result;	
		} catch (SQLException e) {
			if (e.getMessage().toLowerCase().contains("locking") || e.getMessage().toLowerCase().contains("locked")) {
				return retry(query);
			} else {
				this.Error("SQL exception in query(): " + e.getMessage());
			}
			
		}
		return null;
	}


	@Override
	public void close() {
		if (connection != null)
			try {
				connection.close();
			} catch (SQLException ex) {
				this.Error("SQL exception in close(): " + ex);
			}
	}
	
	@Override
	public boolean checkTable(String table) {
		DatabaseMetaData dbm = null;
		try {
			dbm = this.open().getMetaData();
			ResultSet tables = dbm.getTables(null, null, table, null);
			if (tables.next())
			  return true;
			else
			  return false;
		} catch (SQLException e) {
			this.Error("Failed to check if table \"" + table + "\" exists: " + e.getMessage());
			return false;
		}
	}
	
	@Override
	protected boolean initialize() {
		try {
			  Class.forName("org.sqlite.JDBC");
			  
			  return true;
			} catch (ClassNotFoundException e) {
			  log.severe("Class not found in initialize(): " + e.getMessage());
			  return false;
			}
	}
	
	public ResultSet retry(String query) {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			return result;
		} catch (SQLException ex) {
			if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked")) {
				this.Error("Please close your previous ResultSet to run the query: \n\t" + query);
			} else {
				this.Error("SQL exception in retry(): " + ex.getMessage());
			}
		}

		return null;
	}
	
}
