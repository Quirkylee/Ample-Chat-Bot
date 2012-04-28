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
package org.jaggy.bukkit.ample.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

public class MYSQL extends DB {
	public MYSQL(Plugin instance, Logger log,
			 String prefix,
			 String hostname,
			 String portnmbr,
			 String database,
			 String username,
			 String password) {
		super(instance, log,prefix, hostname, portnmbr);
		this.instance = instance;
		this.log = log;
		this.PREFIX = prefix;
		this.dbHost = hostname;
		this.dbPort = portnmbr;
		this.dbName = database;
		this.dbUser = username;
		this.dbPass = password;
	}

	@Override
	protected boolean initialize() {
		try {
			Class.forName("com.mysql.jdbc.Driver"); // Check that server's Java has MySQL support.
			return true;
	    } catch (ClassNotFoundException e) {
	    	this.Error("Class Not Found Exception: " + e.getMessage() + ".");
	    	return false;
	    }
	}

	@Override
	public Connection open() {
		if (initialize()) {
			String url = "";
		    try {
				url = "jdbc:mysql://" + this.dbHost + ":" + this.dbPort + "/" + this.dbName;
				//return DriverManager.getConnection(url, this.username, this.password);
				this.connection = DriverManager.getConnection(url, this.dbUser, this.dbPass);
		    } catch (SQLException e) {
		    	this.Error("Could not be resolved because of an SQL Exception: " + e.getMessage() + ".");
		    }
		}
		return null;
	}

	@Override
	public void close() {
		//Connection connection = open();
				try {
					if (connection != null)
						connection.close();
				} catch (Exception e) {
					this.Error("Failed to close database connection: " + e.getMessage());
				}
	}

	@Override
	public void createTables() {
		if(this.checkTable(this.PREFIX+"Responses") == false) {
		this.query("CREATE TABLE `"+this.dbName+"`.`"+this.PREFIX+
				"Responses` (`id` int AUTO_INCREMENT, `keyphrase` varchar(200), `response` varchar(200), PRIMARY KEY (`id`));");
		}
		if(this.checkTable(this.PREFIX+"Usage") == false) {
			this.query("CREATE TABLE `"+this.dbName+"`.`"+this.PREFIX+"Usage` (`dtime` integer, `question` integer, `player` varchar(50));");
		}
	}

	@Override
	public ResultSet query(String query) {
		//Connection connection = null;
				Statement statement = null;
				ResultSet result = null/*new JdbcRowSetImpl()*/;
				try {
					connection = this.getConnection();
					statement = connection.createStatement();
				    statement = this.connection.createStatement();
				    
				    switch (this.getStatement(query)) {
					    case SELECT:
						    result = statement.executeQuery(query);
						    break;
					    default:
					    	statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
					    	this.lastKeys = statement.getGeneratedKeys();
					    	
				    }
				    //connection.close();
			    	return result;
				} catch (SQLException e) {
					e.printStackTrace();
					this.Warn("Error in SQL query: " + e.getMessage());
				}
				return result;
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
			    default:
			    	statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			    	this.lastKeys = statement.getGeneratedKeys();
			    	
		    }
		    //connection.close();
	    	return result;
		} catch (SQLException e) {
			e.printStackTrace();
			this.Warn("Error in SQL query: " + e.getMessage());
		}
		return result;
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
			e.printStackTrace();
			this.Error("Failed to check if table \"" + table + "\" exists: " + e.getMessage());
			return false;
		}
	}

	@Override
	public Integer currentEpoch() throws SQLException {
		connection = this.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT UNIX_TIMESTAMP(now());");
		return rs.getInt(1);
	}

}
