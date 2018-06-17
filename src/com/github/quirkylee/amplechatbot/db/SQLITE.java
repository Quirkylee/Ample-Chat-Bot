/**
 * Matthewl db handler for Craft Bukkit plugins
 *   Copyright (C) 2012  matthewl

 *  This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.

 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.

 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.quirkylee.amplechatbot.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

public class SQLITE extends DB {
	
	private File sqlFile;

	public SQLITE(Plugin instance, Logger log, String dbHost, String dbName, String prefix) {
		super(instance, log, dbHost, dbName, prefix);
		this.instance = instance;
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
		 this.query("CREATE TABLE \""+this.PREFIX+
				"Responses\" (\"id\" integer NOT NULL ON CONFLICT FAIL PRIMARY KEY AUTOINCREMENT,"+
				 "\"keyphrase\" text(200,0), \"response\" text(200,0));");
		}
		if(this.checkTable(this.PREFIX+"Usage") == false) {
			this.query("CREATE TABLE \""+this.PREFIX+"Usage\" (\"dtime\" integer, \"question\" integer," +
					"\"player\" text(50,0));");
		}
		if(this.checkTable(this.PREFIX+"Spam") == false) {
			this.query("CREATE TABLE \""+this.PREFIX+"Spam\" (\"dtime\" integer, \"action\" integer," +
					"\"player\" text(50,0));");
		}
		if(this.checkTable(this.PREFIX+"Flood") == false) {
			this.query("CREATE TABLE \""+this.PREFIX+"Flood\" (\"dtime\" integer, \"action\" integer," +
					"\"player\" text(50,0));");
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
			    	this.lastKeys = statement.getGeneratedKeys();
			    	break;
				
				default:
					result = statement.executeQuery(query);
			}
			return result;	
		} catch (SQLException e) {
			if (e.getMessage().toLowerCase().contains("locking") || e.getMessage().toLowerCase().contains("locked")) {
				return retry(query);
			} else {
				this.Warn("SQL exception in query(): " + e.getMessage());
			}
			
		}
		return null;
	}
	
	@Override
	public ResultSet preparedStatement(String query, String param1) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = this.getConnection();
			statement = connection.prepareStatement(query);
			statement.setNString(1, param1);
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
		    	this.lastKeys = statement.getGeneratedKeys();
		    	break;
			
			default:
				result = statement.executeQuery(query);
			}
	    	return result;
		} catch (SQLException e) {
			this.Warn("Error in SQL query: " + e.getMessage());
		}
		return result;
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
			dbm = this.connection.getMetaData();
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
				this.Error("Class not found in initialize(): " + e.getMessage());
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
				this.Warn("SQL exception in retry(): " + ex.getMessage());
			}
		}

		return null;
	}

	@Override
	public Integer currentEpoch() throws SQLException {
		connection = this.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT strftime('%s','now');");
		return rs.getInt(1);
	}
	
}
