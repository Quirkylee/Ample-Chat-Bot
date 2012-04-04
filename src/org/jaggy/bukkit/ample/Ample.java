package org.jaggy.bukkit.ample;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jaggy.bukkit.ample.cmds.cmdAmpa;
import org.jaggy.bukkit.ample.cmds.cmdAmple;
import org.jaggy.bukkit.ample.cmds.cmdAmpq;
import org.jaggy.bukkit.ample.config.Config;
import org.jaggy.bukkit.ample.config.YMLFile;
import org.jaggy.bukkit.ample.db.DB;
import org.jaggy.bukkit.ample.db.SQLITE;

public class Ample extends JavaPlugin {
	public class CmdAmple {

	}

	public final Logger log = Logger.getLogger("Minecraft");
	private Config config;
	private DB db = null;
	
	public void loger(String msg) {
		log.info("[Ample] "+msg);
	}
	
	public void onEnable() {
		loadConfig();
		if(config.getDbType().equals("SQLITE")) {
			db = new SQLITE(log,config.getDbHost(),config.getDbName()+".db",config.getDbPrefix());
			loger("Using SQLite db...");
		}
		db.open();
		db.createTables();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new AmpleListener(), this);
		
		getCommand("ample").setExecutor(new cmdAmple());
		getCommand("ampanswer").setExecutor(new cmdAmpa(this));
		getCommand("ampquestion").setExecutor(new cmdAmpq(this));
		
		
	}

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
}
