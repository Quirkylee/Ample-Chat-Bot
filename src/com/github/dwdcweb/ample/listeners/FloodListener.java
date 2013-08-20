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
package com.github.dwdcweb.ample.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import com.github.dwdcweb.ample.Ample;
import com.github.dwdcweb.ample.config.Config;
import com.github.dwdcweb.ample.db.DB;

/**
 * @author matthewl
 *
 */
@SuppressWarnings("unused")
public class FloodListener implements Listener {
	
	private Ample plugin;
	
	private static Config config;
	private DB db;
	private String message;
	
public FloodListener(Ample instance) {
		plugin = instance;
		config = plugin.getDConfig();
		db = plugin.getDB();
	}

@EventHandler(priority = EventPriority.HIGHEST)
void onChat(final AsyncPlayerChatEvent event) {
	Player player = event.getPlayer();
	long currentTime = System.currentTimeMillis();
	long secondWithin = 1000*config.getFloodRatio()[1];
	long floodResetTime = (Long) (player.getMetadata("floodResetTime").isEmpty() ? (currentTime+secondWithin) : player.getMetadata("floodResetTime").get(0).value());
	String update = "";


	if(player.getMetadata("floodResetTime").isEmpty())  {
		player.setMetadata("floodResetTime", new FixedMetadataValue(plugin,floodResetTime));
		player.setMetadata("floodCount", new FixedMetadataValue(plugin,0));
	} else if(currentTime >= floodResetTime) {
		player.setMetadata("floodResetTime", new FixedMetadataValue(plugin,(currentTime+secondWithin)));
		player.setMetadata("floodCount", new FixedMetadataValue(plugin,0));
	}
	player.setMetadata("floodCount", new FixedMetadataValue(plugin,(player.getMetadata("floodCount").get(0).asLong() + 1)));
	if(player.getMetadata("floodCount").get(0).asLong() >= config.getFloodRatio()[0].longValue()) {
		checkRecords(event);
	}
}
void checkRecords(final AsyncPlayerChatEvent event) {
	final Player player = event.getPlayer();
	plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
		@Override
		public void run() {
			try {
				ResultSet rs = db.query("SELECT count(dtime) FROM "+config.getDbPrefix()+"Flood WHERE player = '"+player+"'");
				int v = 0;
				if(v > config.getFloodAction().length) v = rs.getInt(1);
				else if(rs.getInt(1) > 0) v = (config.getFloodAction().length - 1);
				String action = config.getFloodAction()[v];
				if(action.equals("cancel")) {
					event.setCancelled(true);
					insertRecord( 1, player);
				} else if(action.equals("warn")) {
					event.setCancelled(true);
					insertRecord( 2, player);
					player.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"["+config.getBotName()+"] "+config.getFloodWarn());
				} else if(action.equals("kick")) {
					event.setCancelled(true);
					player.kickPlayer(ChatColor.RED+"["+config.getBotName()+"] "+config.getFloodKick());
					insertRecord( 3, player);
				} else if(action.equals("ban")) {
					event.setCancelled(true);
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"ban "+player.getName()+" "+ChatColor.RED+"["+config.getBotName()+"] "+config.getFloodBan());
					insertRecord( 4, player);
				} else if(action.equals("banip")) {
					event.setCancelled(true);
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"banip "+player.getName()+" "+ChatColor.RED+"["+config.getBotName()+"] "+config.getFloodBan());
					insertRecord( 5, player);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	});
	
}

void insertRecord(final int action, final Player player) {
	plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
		@Override
		public void run() {
	try {
		db.query("INSERT INTO "+config.getDbPrefix()+"Flood (dtime,action,player) " +
				"VALUES ( "+db.currentEpoch()+", "+action+", '"+player.getName()+"');");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		}
	});
}
}
