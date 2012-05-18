/**
 * Database Handler:
 * Code is based off of https://github.com/PatPeter/SQLibrary!
 * 
 *
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

public abstract class DB {
	protected Connection connection;
	protected Logger log;
	protected String dbHost;
	protected String dbPort;
	protected String dbUser;
	protected String dbPass;
	protected String dbName;
	protected String PREFIX;
	protected boolean connected;
	public int lastUpdate;
	public ResultSet lastKeys;
	protected Plugin instance;
	
	protected enum Statements {
		SELECT, INSERT, UPDATE, DELETE, DO, REPLACE, LOAD, HANDLER, CALL, // Data manipulation statements
		CREATE, ALTER, DROP, TRUNCATE, RENAME,  // Data definition statements
		
		// MySQL-specific
		START, COMMIT, ROLLBACK, SAVEPOINT, LOCK, UNLOCK, // MySQL Transactional and Locking Statements
		PREPARE, EXECUTE, DEALLOCATE, // Prepared Statements
		SET, SHOW, // Database Administration
		DESCRIBE, EXPLAIN, HELP, USE, // Utility Statements
		
		// SQLite-specific
		ANALYZE, ATTACH, BEGIN, DETACH, END, INDEXED, ON, PRAGMA, REINDEX, RELEASE, VACUUM
	}
	protected Connection getConnection() {
		return connection;
		
	}
	
	public DB(Plugin Instance, Logger log, String dbHost, String dbName, String prefix) {
		this.instance = Instance;
		this.log = log;
		this.dbHost = dbHost;
		this.dbName = dbName;
		this.PREFIX = prefix;
		this.connected = false;
		this.connection = null;
	}
	
	protected abstract boolean initialize();
    public abstract Connection open();
    public abstract void close();
    public abstract Integer currentEpoch() throws SQLException;
    public abstract void createTables();
    public abstract ResultSet query(String msg);
    public abstract ResultSet preparedStatement(String query, String param1);
    
    public void Info(String msg) {
    	this.log.info("[Ample] "+msg);
    }
    public void Warn(String msg) {
    	this.log.warning("[Ample] "+msg);
    }
    public void Error(String msg) {
    	this.log.severe("[Ample] DB error: "+msg);
    }

	public abstract boolean checkTable(String table);
	
	protected Statements getStatement(String query) {
		String trimmedQuery = query.trim();
		if (trimmedQuery.substring(0,6).equalsIgnoreCase("SELECT"))
			return Statements.SELECT;
		else if (trimmedQuery.substring(0,6).equalsIgnoreCase("INSERT"))
			return Statements.INSERT;
		else if (trimmedQuery.substring(0,6).equalsIgnoreCase("UPDATE"))
			return Statements.UPDATE;
		else if (trimmedQuery.substring(0,6).equalsIgnoreCase("DELETE"))
			return Statements.DELETE;
		else if (trimmedQuery.substring(0,6).equalsIgnoreCase("CREATE"))
			return Statements.CREATE;
		else if (trimmedQuery.substring(0,5).equalsIgnoreCase("ALTER"))
			return Statements.ALTER;
		else if (trimmedQuery.substring(0,4).equalsIgnoreCase("DROP"))
			return Statements.DROP;
		else if (trimmedQuery.substring(0,8).equalsIgnoreCase("TRUNCATE"))
			return Statements.TRUNCATE;
		else if (trimmedQuery.substring(0,6).equalsIgnoreCase("RENAME"))
			return Statements.RENAME;
		else if (trimmedQuery.substring(0,2).equalsIgnoreCase("DO"))
			return Statements.DO;
		else if (trimmedQuery.substring(0,7).equalsIgnoreCase("REPLACE"))
			return Statements.REPLACE;
		else if (trimmedQuery.substring(0,4).equalsIgnoreCase("LOAD"))
			return Statements.LOAD;
		else if (trimmedQuery.substring(0,7).equalsIgnoreCase("HANDLER"))
			return Statements.HANDLER;
		else if (trimmedQuery.substring(0,4).equalsIgnoreCase("CALL"))
			return Statements.CALL;
		else
			return Statements.SELECT;
	}
	
	public boolean checkConnection() {
		try {
			connection.isClosed();
			this.connected = false;
		} catch (SQLException e) {
			this.Error("Connection error: "+e);
			this.connected = true;
		}
		return connected;
		
	}
	public int lastID() {
		int id = 0;
		try {
			while (lastKeys.next()) id  = lastKeys.getInt(1);
			return id;
		} catch (SQLException e) {
			return 0;
		}		
	}
	
	public String escape_quotes(String str) {
		String target = "\"";
		
		String replacement = "\"\"";
		str = str.replaceAll(target, replacement);
		
		target = "'";
		replacement = "''";
		str = str.replaceAll(target, replacement);
		return str;
		
		
		
	}
	public String unescape(String str) {
		String target = "\"\"";
		
		String replacement = "\"";
		str = str.replaceAll(target, replacement);
		
		target = "''";
		replacement = "'";
		str = str.replaceAll(target, replacement);
		return str;
		
		
		
	}
}
