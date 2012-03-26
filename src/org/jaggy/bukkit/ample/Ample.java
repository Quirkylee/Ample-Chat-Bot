package org.jaggy.bukkit.ample;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jaggy.bukkit.ample.cmds.cmdAmpa;
import org.jaggy.bukkit.ample.cmds.cmdAmple;
import org.jaggy.bukkit.ample.cmds.cmdAmpq;

public class Ample extends JavaPlugin {
	public class CmdAmple {

	}

	private static final Logger log = Logger.getLogger("Minecraft");
	
	public void log(String msg) {
		log.info("[Ample] "+msg);
	}
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new AmpleListener(), this);
		
		getCommand("ample").setExecutor(new cmdAmple());
		getCommand("ampanswer").setExecutor(new cmdAmpa());
		getCommand("ampquestion").setExecutor(new cmdAmpq());
	}
	
	public void onDisable() {
		
	}

}
