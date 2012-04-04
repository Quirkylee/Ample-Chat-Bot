package org.jaggy.bukkit.ample.config;

import org.jaggy.bukkit.ample.Ample;


public interface Config {
	public String defaultDbType = "SQLITE";
	public String defaultDbHost = "plugins/Ample/";
	public String defaultDbPort= "";
	public String defaultDbName = "Ample.db";
	public String defaultDbPrefix = "Ample_";
	public String defaultDbUser = "Ample";
	public String defaultDbPass = "Minecraft";
	public String defaultBotName = "AmpleBot";
	public String defaultDisplay = "<%botname> %message";
	
	public void save() throws Exception;
	
	public String getDbType();
	public String getDbHost();
	public String getDbPort();
	public String getDbName();
	public String getDbPrefix();
	public String getDbUser();
	public String getDbPass();
	public String getBotName();
	public String getDisplay();
	void load(Ample instance) throws Exception;
}
