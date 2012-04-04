package org.jaggy.bukkit.ample.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jaggy.bukkit.ample.Ample;

public class YMLFile implements Config {

	private JavaPlugin plugin;
	private FileConfiguration bukkitConfig;
	
	@Override
	public void load(Ample instance) {
		plugin = instance;
		
		// this forces Bukkit to load the config
		bukkitConfig = plugin.getConfig();
	}
	
	@Override
	public void save() throws Exception {
		plugin.saveConfig();
	}

	@Override
	public String getDbType() {
		return bukkitConfig.getString("DbType", defaultDbType);
	}

	@Override
	public String getDbHost() {
		return bukkitConfig.getString("DbHost", defaultDbHost);
	}

	@Override
	public String getDbPort() {
		return bukkitConfig.getString("DbPort", defaultDbPort);
	}

	@Override
	public String getDbName() {
		return bukkitConfig.getString("DbName", defaultDbName);
	}
	
	@Override
	public String getDbPrefix() {
		return bukkitConfig.getString("DbPrefix", defaultDbPrefix);
	}
	
	@Override
	public String getDbUser() {
		return bukkitConfig.getString("DbUser", defaultDbUser);
	}

	@Override
	public String getDbPass() {
		return bukkitConfig.getString("DbPass", defaultDbPass);
	}

	@Override
	public String getBotName() {
		return bukkitConfig.getString("BotName", defaultBotName);
	}

	@Override
	public String getDisplay() {
		return bukkitConfig.getString("Display", defaultDisplay);
	}

}
