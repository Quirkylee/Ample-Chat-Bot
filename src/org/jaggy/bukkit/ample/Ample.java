/**
 * Ample Chat Bot is a chat bot plugin for Craft Bukkit Servers
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

package org.jaggy.bukkit.ample;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jaggy.bukkit.ample.MetricsLite;
import org.jaggy.bukkit.ample.cmds.CmdSay;
import org.jaggy.bukkit.ample.cmds.CmdAmpa;
import org.jaggy.bukkit.ample.cmds.CmdAmpd;
import org.jaggy.bukkit.ample.cmds.CmdAmple;
import org.jaggy.bukkit.ample.cmds.CmdQuestion;
import org.jaggy.bukkit.ample.cmds.CmdQList;
import org.jaggy.bukkit.ample.config.Config;
import org.jaggy.bukkit.ample.config.YMLFile;
import org.jaggy.bukkit.ample.db.DB;
import org.jaggy.bukkit.ample.db.MYSQL;
import org.jaggy.bukkit.ample.db.SQLITE;

public class Ample extends JavaPlugin {


	public final Logger log = Logger.getLogger("Minecraft");
	private Config config;
	private DB db = null;
	public boolean essentialsEnable = false;
	
	public void loger(String msg) {
		log.info("[Ample] "+msg);
	}
	
	@Override
	public void onEnable() {
		//mcstats plugin
		try {
		    MetricsLite metrics = new MetricsLite(this);
		    metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		    // Failed to submit the stats :-(
		}
		//load config
		loadConfig();
		
		//test for essentials
		Plugin essentials = this.getServer().getPluginManager().getPlugin("Essentials");
	    if( essentials != null )
	    {
	        loger("Essentials found.....");
	        essentialsEnable  = true;
	    }
		
		//initialize db
		if(config.getDbType().equals("SQLITE")) {
			db = new SQLITE(this, log,config.getDbHost(),config.getDbName()+".db",config.getDbPrefix());
			loger("Using SQLite db...");
		}
		if(config.getDbType().equals("MYSQL")) {
			db= new MYSQL(this, log,
					config.getDbPrefix(),
					config.getDbHost(),
					config.getDbPort(),
					config.getDbName(),
					config.getDbUser(),
					config.getDbPass());
			loger("Using MySQL db...");
		}
		db.open();
		db.createTables();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new AmpleListener(this), this);
		
		getCommand("ample").setExecutor(new CmdAmple(this));
		getCommand("answer").setExecutor(new CmdAmpa(this));
		getCommand("qlist").setExecutor(new CmdQList(this));
		getCommand("question").setExecutor(new CmdQuestion(this));
		getCommand("delquestion").setExecutor(new CmdAmpd(this));
		getCommand("amplesay").setExecutor(new CmdSay(this));
		
		
	}

	@Override
	public void onDisable() {
		db.close();
	}
	
	
	/**
	 * Loads the config into memory.
	 */
	public void loadConfig() {
		
		File File = new File("plugins/Ample/config.yml");
		if( File.exists() ) {		// new-style config.yml exists?  use it
    		config = new YMLFile();
    	} else {							// neither exists yet (new installation), create and use new-style
    		this.saveDefaultConfig();
    		config = new YMLFile();
    	}
		try {
    		config.load(this);
    	}
    	catch(Exception e) {
            loger("an error occured while trying to load the config file.");
    		e.printStackTrace();
    	}
				
	}
	/**
	 * Returns the instance of the current config.
	 * 
	 * @return Config
	 */
	public Config getDConfig() { return config; }
	/**
	 * Returns the instance of the database loaded.
	 * @return DB
	 */
	public DB getDB() {
		return db;
	}
	
	/**
	 * Sends message to Command Sender.
	 * 
	 * @param sender
	 * @param msg
	 */
	public void Msg (CommandSender sender, String msg) {
		if(sender instanceof Player) {
			
			sender.sendMessage(ChatColor.GREEN+msg);
		}
		else log.info("[Ample] " +msg);
	}
	/**
	 * Sends message to Player.
	 * 
	 * @param sender
	 * @param msg
	 */
	public void Msg (Player sender, String msg) {
		sender.sendMessage(ChatColor.GREEN+msg);
	}

	/**
	 * @param sender
	 * @param msg
	 */
	public void Error(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.RED+msg);
	}
}
