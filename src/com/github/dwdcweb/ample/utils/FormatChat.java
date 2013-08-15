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
package com.github.dwdcweb.ample.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author matthewl
 *
 */
public final class FormatChat {
	
	public final static String setDisplay(String display, String message, String botname) {
		String str = display.replaceAll("%botname", botname);	
		return str.replaceAll("%message", message);
	}
	
	public static String formatChat(String chat, AsyncPlayerChatEvent event)
	{
		//format chat
		chat = ChatColor.translateAlternateColorCodes('&',chat);

		//set wildcards
		chat = chat.replaceAll("%player", event.getPlayer().getName());
		
		//numerical wildcards
		String[] words =ChatColor.stripColor(event.getMessage()).split(" ");
		for(int i=0;i < words.length; i++) chat = chat.replace("%"+(i+1), words[i]);

		return chat;
	}
	public static String formatChat(String chat, CommandSender sender)
	{
		//format chat
		chat = ChatColor.translateAlternateColorCodes('&',chat);

		//set wildcards
		chat = chat.replaceAll("%player", sender.getName());
		
		//numerical wildcards
		String[] words =ChatColor.stripColor(chat).split(" ");
		for(int i=0;i < words.length; i++) chat = chat.replace("%"+(i+1), words[i]);

		return chat;
	}

}
