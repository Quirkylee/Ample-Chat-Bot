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
package org.jaggy.bukkit.ample.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jaggy.bukkit.ample.Ample;

public class cmdAmple implements CommandExecutor {

	private Ample plugin = new Ample();
	
	public cmdAmple(Ample instance) {
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		plugin.Msg(sender, "Ample Chat Bot is a chat robot that responses");
		plugin.Msg(sender, "to key phrases people say in chat.");
		plugin.Msg(sender, "Wildcards:");
		plugin.Msg(sender, "   %botname = Bot's name.");
		plugin.Msg(sender, "   %player = Player invoking response.");
		plugin.Msg(sender, "   * Chat Colors are supported.");
		plugin.Msg(sender, " ");
		plugin.Msg(sender, "/answer <question id> <answer>");
		plugin.Msg(sender, "   Sets the response to the question or keyphrase.");
		plugin.Msg(sender, " ");
		plugin.Msg(sender, "/question <question or keyphrase>");
		plugin.Msg(sender, "   Adds an question or keyphrase. Minimum characters is 3.");
		plugin.Msg(sender, " ");
		plugin.Msg(sender, "/qlist <QID|keyphrase or question>");
		plugin.Msg(sender, "   Searchs/Lists question or keyphrase.");
		plugin.Msg(sender, "   Use % to search all or todo partial keyphrases.");
		plugin.Msg(sender, " ");
		plugin.Msg(sender, "/delquestion <question id>");
		plugin.Msg(sender, "   Deletes question or keyphrase.");
		return true;
	}

}
