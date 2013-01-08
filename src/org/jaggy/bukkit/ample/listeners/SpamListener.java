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
package org.jaggy.bukkit.ample.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jaggy.bukkit.ample.Ample;
import org.jaggy.bukkit.ample.config.Config;
import org.jaggy.bukkit.ample.db.DB;

/**
 * @author matthewl
 *
 */
public class SpamListener implements Listener {
	
	private Ample plugin;
	private static Config config;
	private DB db;
	private String message;
	public SpamListener(Ample instance) {
		plugin = instance;
		config = plugin.getDConfig();
		db = plugin.getDB();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	void onChat(AsyncPlayerChatEvent event) {
			message = event.getMessage();
			Player player = event.getPlayer();
			final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

			Pattern p = Pattern.compile(URL_REGEX);
			Matcher m = p.matcher(message);//replace with string to compare
			if(m.find()) {
				try {
					ResultSet rs = db.query("SELECT count(dtime) FROM "+config.getDbPrefix()+"Spam WHERE player = '"+player.getName()+"'");
					int v = 0;
					if(v > config.getSpamAction().length) v = (config.getSpamAction().length - 1);
					else if(rs.getInt(1) > 0) v = rs.getInt(1);
					String action = config.getSpamAction()[v];
					if(action.equals("cancel")) {
						event.setCancelled(true);
						insertRecord( 1, player);
					} else if(action.equals("warn")) {
						event.setCancelled(true);
						insertRecord( 2, player);
						player.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"["+config.getBotName()+"] "+config.getSpamWarn());
					} else if(action.equals("kick")) {
						event.setCancelled(true);
						player.kickPlayer(ChatColor.RED+"["+config.getBotName()+"] "+config.getSpamKick());
						insertRecord( 3, player);
					} else if(action.equals("ban")) {
						event.setCancelled(true);
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"ban "+player.getName()+" "+ChatColor.RED+"["+config.getBotName()+"] "+config.getSpamBan());
						insertRecord( 4, player);
					} else if(action.equals("banip")) {
						event.setCancelled(true);
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"banip "+player.getName()+" "+ChatColor.RED+"["+config.getBotName()+"] "+config.getSpamBan());
						insertRecord( 5, player);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
	}
	
	void insertRecord(int action, Player player) throws SQLException {
		db.query("INSERT INTO "+config.getDbPrefix()+"Spam (dtime,action,player) " +
				"VALUES ( "+db.currentEpoch()+", "+action+", '"+player.getName()+"');");
	}
}
