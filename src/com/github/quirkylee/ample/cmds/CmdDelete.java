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

package com.github.quirkylee.ample.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.quirkylee.ample.Ample;
import com.github.quirkylee.ample.config.Config;
import com.github.quirkylee.ample.db.DB;

public class CmdDelete implements CommandExecutor {

	private Ample plugin;
	private DB db;
	private Config config;
	
	public CmdDelete(Ample instance) {
		plugin = instance;
		db = plugin.getDB();
		config = plugin.getDConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if( player.hasPermission("ample.delete") ) {
			delQuestions(sender, args);	
			} else plugin.Error(player, "You do not have permissions to use this command.");
		} else {
			delQuestions(sender, args);
		}
		return true;
	}

	private void delQuestions(CommandSender sender, String[] args) {
		if(args.length < 1) {
			plugin.Msg(sender, "Please enter in a Question ID: /delquestion <QID>");
		} else {
			db.query("DELETE FROM "+config.getDbPrefix()+"Responses WHERE id = '"+args[0]+"';");
			plugin.Msg(sender, "Question was deleted.");
			
		}
		
		
	}

}
