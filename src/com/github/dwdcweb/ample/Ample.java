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

package com.github.dwdcweb.ample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.dwdcweb.ample.cmds.CmdAmple;
import com.github.dwdcweb.ample.cmds.CmdAnswer;
import com.github.dwdcweb.ample.cmds.CmdDelete;
import com.github.dwdcweb.ample.cmds.CmdQList;
import com.github.dwdcweb.ample.cmds.CmdQuestion;
import com.github.dwdcweb.ample.cmds.CmdSay;
import com.github.dwdcweb.ample.cmds.CmdUpdate;
import com.github.dwdcweb.ample.config.Config;
import com.github.dwdcweb.ample.config.YMLFile;
import com.github.dwdcweb.ample.db.DB;
import com.github.dwdcweb.ample.db.MYSQL;
import com.github.dwdcweb.ample.db.SQLITE;
import com.github.dwdcweb.ample.listeners.FloodListener;
import com.github.dwdcweb.ample.listeners.PlayerListener;
import com.github.dwdcweb.ample.listeners.ResponseListener;
import com.github.dwdcweb.ample.listeners.SpamListener;

public class Ample extends JavaPlugin {


	public final Logger log = Logger.getLogger("AmpleChatBot");
	private Config config;
	private DB db = null;
	public boolean essentialsEnable = false;
	public boolean checkupdate;
	
	public String version;
	public String newversion;
	public String verinfo;
	
	public void loger(String msg) {
		log.info("[AmpleChatBot] "+msg);
	}
	
	@Override
	public void onEnable() {
		version = getDescription().getVersion();
		
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
		
		//create the db.yml example
		createDbYml();
		//check update
		setUpdateInfo();
		if (!version.equals(newversion) && !version.contains("TEST") && !(newversion == null))
			send("An update for Ample chat bot is available! "+"Current version: "+version+" New version: "+newversion);
		
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
		
		//command registers
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new ResponseListener(this), this);
		pm.registerEvents(new SpamListener(this), this);
		pm.registerEvents(new FloodListener(this), this);
		pm.registerEvents(new PlayerListener(this), this);
		
		getCommand("ample").setExecutor(new CmdAmple(this));
		getCommand("answer").setExecutor(new CmdAnswer(this));
		getCommand("qlist").setExecutor(new CmdQList(this));
		getCommand("question").setExecutor(new CmdQuestion(this));
		getCommand("delquestion").setExecutor(new CmdDelete(this));
		getCommand("amplesay").setExecutor(new CmdSay(this));
		getCommand("ampleupdate").setExecutor(new CmdUpdate(this));
		
		
	}

	@Override
	public void onDisable() {
		db.close();
	}
	
	
	/**
	 * Loads the config into memory.
	 */
	public void loadConfig() {
		
		File File = new File("plugins/AmpleChatBot/config.yml");
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
	
	void createDbYml() {
		File File = new File("plugins/AmpleChatBot/db.yml");
		if( !File.exists() ) {
			try {
				send("Creating the example db.yml file for the first time! You can now use that file with /ample db import or /ample db export");
				File.createNewFile();
				String content ="\"Can I be op\":\n   answer: 'Sorry! We only op people we trust!'\n   permission: 'ample.group.*'";
				FileWriter fw = new FileWriter(File.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(content);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
		else log.info("[AmpleChatBot] " +msg);
	}
	
	public void setUpdateInfo() {
		if (config.getCheckUpdate()) {
			try {
				URL url = new URL("https://raw.github.com/dwdcweb/Ample-Chat-Bot/master/version.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
				if (br != null) {
					for(int i = 0; i < 3; ++i)
						br.readLine();
					String newversionraw = br.readLine();
						newversion = newversionraw.replace("version: ", "");
					br.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
	
	public void send(String message) {
		System.out.println("[AmpleChatBot] "+message);
	}

	/**
	 * @param sender
	 * @param msg
	 */
	public void Error(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.RED+msg);
	}
	
}
