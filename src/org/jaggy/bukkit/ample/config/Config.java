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
	public String defaultAbuseRatio = "3;20";
	public double defaultAllowable = 80;
	public String defaultAbuseAction = "ignore";
	public String defaultAbuseKick = "[AmpleBot] Do not abuse me or I will keep kicking you!";
	public String defaultDisplay = "<%botname> %message";
	public long defaultMsgDelay = 2;
	
	public void save() throws Exception;
	
	public String getDbType();
	public String getDbHost();
	public String getDbPort();
	public String getDbName();
	public String getDbPrefix();
	public String getDbUser();
	public String getDbPass();
	public String getBotName();
	public Integer[] getAbuseRatio();
	public Double getAllowable();
	public String getAbuseAction();
	public String getAbuseKick();
	public String getDisplay();
	public Long getMsgDelay();
	void load(Ample instance) throws Exception;
}
