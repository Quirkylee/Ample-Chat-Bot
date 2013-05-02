package org.jaggy.bukkit.ample.config;

import org.jaggy.bukkit.ample.Ample;


public interface Config {
	public String defaultDbType = "SQLITE";
	public String defaultDbHost = "plugins/AmpleChatBot/";
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
	public String defaultIRCChannels = "#AmpleChatBot";
	public String defaultSpamAction = "warn,kick,ban";
	public String defaultSpamWarn = "Please do not spam!";
	public String defaultSpamKick = "Please do not spam!";
	public String defaultSpamBan = "Your banned for spamming!";
	public String defaultFloodAction = "warn,kick,ban";
	public String defaultFloodWarn = "Please do not flood!";
	public String defaultFloodKick = "Please do not flood!";
	public String defaultFloodBan = "Your banned for flooding!";
	public String defaultFloodRatio = "5;10";
	public boolean checkUpdate = true;
	public long defaultMsgDelay = 5;
	
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
	public String getIRCChannels();
	public String getSpamWarn();
	public String[] getFloodAction();
	public String getFloodWarn();
	public String getFloodKick();
	public String getFloodBan();
	public boolean getCheckUpdate();
	public Long getMsgDelay();
	void load(Ample instance) throws Exception;
	public Integer[] getFloodRatio();

	/**
	 * @param path
	 * @param obj
	 */
	void set(String path, Object obj);
}
