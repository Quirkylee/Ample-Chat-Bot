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
package com.github.quirkylee.ample.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.quirkylee.ample.Ample;
import com.github.quirkylee.ample.config.Config;
import com.github.quirkylee.ample.utils.FormatChat;


/**
 * @author matthewl
 *
 */
public class CmdSay implements CommandExecutor {

	private Ample plugin;
	private Config config;
	
	public CmdSay(Ample instance) {
		plugin = instance;
		config = plugin.getDConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if( player.hasPermission("ample.say") ) {
				sayMsg(sender, args, label);	
			} else plugin.Error(player, "You do not have permissions to use this command.");
		} else {
			sayMsg(sender, args, label);
		}
		return true;
	}
	
	public void sayMsg(CommandSender sender, String[] args, String label) {
		if(args.length > 0) {
		String msg = "";
		for(int i = 0; i < args.length; i++) {
			msg += args[i];
			msg += " ";
		}
		msg = FormatChat.setDisplay(config.getDisplay(), msg, config.getBotName());
		msg = FormatChat.formatChat(msg, sender);
		plugin.getServer().broadcastMessage(msg);
		} else plugin.Error(sender, "Needs the message. /"+label+" <message>");
	}
}
