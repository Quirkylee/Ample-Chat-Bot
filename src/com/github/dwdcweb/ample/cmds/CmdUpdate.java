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
package com.github.dwdcweb.ample.cmds;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.dwdcweb.ample.Ample;
import com.github.dwdcweb.ample.config.Config;
import com.github.dwdcweb.ample.db.DB;
import com.github.dwdcweb.ample.utils.Misc;

public class CmdUpdate implements CommandExecutor {
	
	private Ample plugin = new Ample();
	private DB db;
	private Config config;
	
	public CmdUpdate(Ample instance) {
		plugin = instance;
		db = plugin.getDB();
		config = plugin.getDConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(args.length >= 2) {
			if(Misc.isInteger(args[0])) {
				String question = "";
				int id = Integer.parseInt(args[0]);

				for(int i = 1; i < args.length; i++) {
					question += args[i];
					question += " ";
				}
				question = question.trim();
				if(sender instanceof Player){
					Player player = (Player) sender;
					if( player.hasPermission("ample.edit") ) {
						try {
							updateQuestion(sender, id, question);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							db.Error("DB error:" +e);
						}

					} else plugin.Error(player, "You do not have permissions to use this command.");
				} else {
					try {
						updateQuestion(sender, id, question);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						db.Error("DB error:" +e);
					}
				} 
			} else plugin.Msg(sender, "Usage: /"+label+" <qid> <new question or keyphrase>");
		} else plugin.Msg(sender, "Usage: /"+label+" <qid> <new question or keyphrase>");
		return true;
	}

	private void updateQuestion(CommandSender sender, int id, String question) throws SQLException {
		if(question.length() >= 3) {

		if(db.query("UPDATE "+config.getDbPrefix()+"Responses SET keyphrase = '"+db.escape_quotes(question.toLowerCase())+"' WHERE id = "+id+";") != null) 
			plugin.Msg(sender, "Db error: Failed to updated the question.");
		else
			plugin.Msg(sender, "Question Has been updated!");
		} else plugin.Msg(sender, "Question keyphrase is to short it has to be 4 characters or greater.");
	}
	
}
