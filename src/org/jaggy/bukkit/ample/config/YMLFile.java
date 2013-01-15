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

	@Override
	public Integer[] getAbuseRatio() {
		String[] ratio = bukkitConfig.getString("AbuseRatio", defaultAbuseRatio).split(";");
		Integer v1 = Integer.parseInt(ratio[0]);
		Integer v2 = Integer.parseInt(ratio[1]);
		Integer[] ary = {v1, v2};
		return  ary;
	}

	@Override
	public String getAbuseAction() {
		return bukkitConfig.getString("AbuseAction", defaultAbuseAction);
	}

	@Override
	public String getAbuseKick() {
		return bukkitConfig.getString("AbuseKick", defaultAbuseKick);
	}

	@Override
	public Long getMsgDelay() {
		return bukkitConfig.getLong("Delay", defaultMsgDelay);
	}

	/* (non-Javadoc)
	 * @see org.jaggy.bukkit.ample.config.Config#getAllowable()
	 */
	@Override
	public Double getAllowable() {
		return bukkitConfig.getDouble("Allowable", defaultAllowable);
	}

	/* (non-Javadoc)
	 * @see org.jaggy.bukkit.ample.config.Config#getIRCChannels()
	 */
	@Override
	public String getIRCChannels() {
		return bukkitConfig.getString("IRCChannels", defaultIRCChannels);
	}

	/* (non-Javadoc)
	 * @see org.jaggy.bukkit.ample.config.Config#getSpamAction()
	 */
	@Override
	public String[] getSpamAction() {
		return bukkitConfig.getString("SpamAction", defaultSpamAction).split(",");
	}

	/* (non-Javadoc)
	 * @see org.jaggy.bukkit.ample.config.Config#getSpamWarn()
	 */
	@Override
	public String getSpamWarn() {
		return bukkitConfig.getString("SpamWarn", defaultSpamWarn);
	}

	/* (non-Javadoc)
	 * @see org.jaggy.bukkit.ample.config.Config#getSpamKick()
	 */
	@Override
	public String getSpamKick() {
		return bukkitConfig.getString("SpamKick", defaultSpamKick);
	}

	/* (non-Javadoc)
	 * @see org.jaggy.bukkit.ample.config.Config#getSpamBan()
	 */
	@Override
	public String getSpamBan() {
		return bukkitConfig.getString("SpamBan", defaultSpamBan);
	}

	/* (non-Javadoc)
	 * @see org.jaggy.bukkit.ample.config.Config#getFloodAction()
	 */
	@Override
	public String[] getFloodAction() {
		return bukkitConfig.getString("FloodAction", defaultFloodAction).split(",");
	}

	/* (non-Javadoc)
	 * @see org.jaggy.bukkit.ample.config.Config#getFloodWarn()
	 */
	@Override
	public String getFloodWarn() {
		return bukkitConfig.getString("FloodWarn", defaultFloodWarn);
	}

	/* (non-Javadoc)
	 * @see org.jaggy.bukkit.ample.config.Config#getFloodKick()
	 */
	@Override
	public String getFloodKick() {
		return bukkitConfig.getString("FloodKick", defaultFloodKick);
	}

	/* (non-Javadoc)
	 * @see org.jaggy.bukkit.ample.config.Config#getFloodBan()
	 */
	@Override
	public String getFloodBan() {
		return bukkitConfig.getString("FloodBan", defaultFloodBan);
	}
	@Override
	public Integer[] getFloodRatio() {
		String[] ratio = bukkitConfig.getString("FloodRatio", defaultFloodRatio).split(";");
		Integer v1 = Integer.parseInt(ratio[0]);
		Integer v2 = Integer.parseInt(ratio[1]);
		Integer[] ary = {v1, v2};
		return  ary;
	}
	@Override
	public void set(String path, Object obj) {
		bukkitConfig.set(path, obj);
	}
}
