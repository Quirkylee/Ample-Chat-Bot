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
package org.jaggy.bukkit.ample;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.jaggy.bukkit.ample.config.Config;
import org.jaggy.bukkit.ample.db.DB;

public class AmpleListener implements Listener {
	private Ample plugin;
	private static Config config;
	private DB db;
	private String message;
	AmpleListener(Ample instance) {
		plugin = instance;
		config = plugin.getDConfig();
		db = plugin.getDB();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	void onChat(final PlayerChatEvent event) {
		if( event.getPlayer().hasPermission("ample.invoke") ) {
			message = ChatColor.stripColor(event.getMessage()).toLowerCase();
			if(message.length() >= 3) {
				ResultSet result = db.query("SELECT * FROM "+config.getDbPrefix()+"Responses ORDER BY keyphrase DESC;");
					try {
					    TreeMap<Double,String> rank = new TreeMap<Double,String>();
						while(result.next()) {
							String response = result.getString("keyphrase").toLowerCase();
							double reslength = response.length();
							double msglength = message.length();
							double rel;
							if(reslength >= msglength) rel = ((msglength/reslength)*100);
							else rel = ((reslength/msglength)*100);
							String[] mary = message.split(" ");
							double count = 0;
							for(int i=0;i < mary.length;i++) {
								if(response.contains(mary[i])) count ++;
							}
							double wordrel;
							if (count <= mary.length) wordrel = ((count/mary.length)*100);
							else wordrel = ((mary.length/count)*100);
							double avgrel = ((wordrel+rel)/2);
							rank.put(avgrel, response);
							
						}
						Entry<Double, String> highest = rank.lastEntry();
						plugin.loger("String rel:"+highest.getKey()+" = "+highest.getValue());
						if(highest.getKey() > 80) {
						execute(result, event);
						}
					} catch (SQLException e) {
						
					}
			}
		}
	}
	/**
	 * @param result
	 * @throws SQLException 
	 */
	private void execute(final ResultSet result, final PlayerChatEvent event) throws SQLException {
		String response = result.getString("response");
		int id = result.getInt("id");
		db.query("INSERT INTO "+config.getDbPrefix()+"Usage (player,dtime,question) " +
				"VALUES ( '"+event.getPlayer().getName()+"', "+db.currentEpoch()+", "+id+");");
		ResultSet rs = db.query("SELECT COUNT(dtime) FROM "+config.getDbPrefix()+"Usage WHERE question = "+id+" AND dtime < "+db.currentEpoch()+" AND dtime > "+(db.currentEpoch() - config.getAbuseRatio()[1])+";");
		int v = rs.getInt(1);
		int c = config.getAbuseRatio()[0];
		if(v <= c) {
			response = formatChat(setDisplay(config.getDisplay(), response, config.getBotName()), event);
			final String fmsg = response;
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

				@Override
				public void run() {
					try {
						if(result.getString("response").toLowerCase().substring(0, 4).equals("cmd:")) {
							String cmd = db.unescape(result.getString("response").toLowerCase().substring(4));
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),formatChat(cmd.trim(),event));
						} else if(result.getString("response").toLowerCase().substring(0, 5).equals("pcmd:")) {
							String cmd = db.unescape(result.getString("response").toLowerCase().substring(5));
							Bukkit.getServer().dispatchCommand(event.getPlayer(),formatChat(cmd.trim(),event));
						} else {
							plugin.getServer().broadcastMessage(db.unescape(fmsg));
						}
					} catch (CommandException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, config.getMsgDelay());	
		} else {
			if(config.getAbuseAction().equalsIgnoreCase("kick")) event.getPlayer().kickPlayer(config.getAbuseKick());
		}
	}

	public String setDisplay(String display, String message, String botname) {
		String str = display.replaceAll("%botname", botname);	
		return str.replaceAll("%message", message);
	}
	public static String formatChat(String chat, PlayerChatEvent event)
	{
		// Weight & Style
		chat = chat.replaceAll("&l", ChatColor.BOLD + "");
		chat = chat.replaceAll("&o", ChatColor.ITALIC + "");
		chat = chat.replaceAll("&n", ChatColor.UNDERLINE + "");
		chat = chat.replaceAll("&m", ChatColor.STRIKETHROUGH + "");

		// Reset
		chat = chat.replaceAll("&r", ChatColor.RESET + "");

		// Colours
		chat = chat.replaceAll("&0", ChatColor.BLACK + "");
		chat = chat.replaceAll("&1", ChatColor.DARK_BLUE + "");
		chat = chat.replaceAll("&2", ChatColor.DARK_GREEN + "");
		chat = chat.replaceAll("&3", ChatColor.DARK_AQUA + "");
		chat = chat.replaceAll("&4", ChatColor.DARK_RED + "");
		chat = chat.replaceAll("&5", ChatColor.DARK_PURPLE + "");
		chat = chat.replaceAll("&6", ChatColor.GOLD + "");
		chat = chat.replaceAll("&7", ChatColor.GRAY + "");
		chat = chat.replaceAll("&8", ChatColor.DARK_GRAY + "");
		chat = chat.replaceAll("&9", ChatColor.BLUE + "");
		chat = chat.replaceAll("&a", ChatColor.GREEN + "");
		chat = chat.replaceAll("&b", ChatColor.AQUA + "");
		chat = chat.replaceAll("&c", ChatColor.RED + "");
		chat = chat.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
		chat = chat.replaceAll("&e", ChatColor.YELLOW + "");
		chat = chat.replaceAll("&f", ChatColor.WHITE + "");

		// Magic
		chat = chat.replaceAll("&k", ChatColor.MAGIC + "");
		//set wildcards
		chat = chat.replaceAll("%player", event.getPlayer().getName());
		
		//numerical wildcards
		String[] words =ChatColor.stripColor(event.getMessage()).split(" ");
		for(int i=0;i < words.length; i++) chat = chat.replace("%"+(i+1), words[i]);

		return chat;
	}

}
