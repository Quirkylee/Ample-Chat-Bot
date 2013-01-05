/**
 * Ample Chat Bot is a chat bot plugin for Craft Bukkit Servers
 *   Copyright (C) 2013  matthewl

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
package org.jaggy.bukkit.ample.hooks;

import org.jaggy.bukkit.ample.Ample;
import org.jaggy.bukkit.ample.config.Config;
import org.monstercraft.irc.MonsterIRC;
import org.monstercraft.irc.plugin.handles.IRCHandler;
/**
 * @author matthewl
 *
 */
public class MonsterIRCHook {
	private Ample plugin;
	private IRCHandler irchandler;
	private Config config;


	public MonsterIRCHook(Ample pl) {
		plugin  = pl;
	    config = plugin.getDConfig();
	}
	public void loadHook() {
		if(plugin.getServer().getPluginManager().getPlugin("MonsterIRC") != null) {
			plugin.loger("Enabling MonsterIRC Support!");
			irchandler = MonsterIRC.getHandleManager().getIRCHandler();
		}
	}
	public void sendToIRC(String msg) {
		String channels = config.getIRCChannels();
		String[] channel = channels.split(",");
		for(int i = 0; i < channel.length; i++) {
		irchandler.sendMessage(channel[i], msg);
		}
	}
	public IRCHandler getIRCHandler() {
		return irchandler;
	}
	public boolean isConnected() {
		
		return MonsterIRC.getHandleManager().getIRCHandler().isConnected();
	}
	
}
