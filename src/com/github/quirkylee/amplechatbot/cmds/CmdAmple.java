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
package com.github.quirkylee.amplechatbot.cmds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.yaml.snakeyaml.Yaml;

import com.github.quirkylee.amplechatbot.Ample;

public class CmdAmple implements CommandExecutor {

	private Ample plugin;

	public CmdAmple(Ample instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(args.length != 0) {
		if(args[0].equals("help")) {
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
			plugin.Msg(sender, " ");
			plugin.Msg(sender, "/amplesay <message>");
			plugin.Msg(sender, "   Makes the Chat Bot say something.");
			plugin.Msg(sender, " ");
			plugin.Msg(sender, "/ampleupdate <question id> <new question or keyphrase>");
			plugin.Msg(sender, "   Updates an existing question or keyphrase.");
		} else if(args[0].equals("config") && args.length > 0) {
			Config config = new Config();
			config.cmd(sender, args);
		} else if(args[0].equals("db") && args.length > 0) {
			Db db = new Db();
			db.cmd(sender, args);
			}
		} else {
			plugin.Msg(sender, "|---Ample chat bot version "+plugin.version+"---|");
			plugin.Msg(sender, "");
			plugin.Msg(sender, "/ample help :"+ChatColor.DARK_GREEN+" Shows how to use the other commands.");
			plugin.Msg(sender, "/ample config :"+ChatColor.DARK_GREEN+" Manages plugin configuration file.");
			plugin.Msg(sender, "/ample db:"+ChatColor.DARK_GREEN+" Tool to export and import database.");
		}
		return true;
	}

	class Config {
		String[] flags = {"BotName", "Delay", "Allowable", "IRCChannels", "AbuseRatio",
				"AbuseAction", "AbuseKick", "SpamAction", "SpamWarn", "SpamKick", "SpamBan",
				"FloodAction", "FloodRatio", "FloodWarn", "FloodKick", "FloodBan", "DbType",
				"DbHost", "DbPort", "DbName", "DbPrefix", "DbUser", "DbPass"};
		public void cmd(CommandSender sender, String[] args) {
			if(args.length >= 2) {
				if(args[1].equalsIgnoreCase("set")) {
					set(sender, args);
				} else if(args[1].equalsIgnoreCase("save")) {
					save(sender, args);
				} else if(args[1].equalsIgnoreCase("reload")) {
					reload(sender, args);
				} else if(args[1].equalsIgnoreCase("list")) {
					list(sender, args);
				} 
			} else {
				plugin.Msg(sender, "This command manages configuration file and settings!");
				plugin.Msg(sender, "");
				plugin.Msg(sender, "/ample config set <setting> <value>");
				plugin.Msg(sender, "/ample config save");
				plugin.Msg(sender, "/ample config reload");
				plugin.Msg(sender, "/ample config list");
			}
		}

		private void reload(CommandSender sender, String[] args) {
			try {
				plugin.getConfig().load("plugins/AmpleChatBot/config.yml");
				plugin.Msg(sender, "The config was reloaded!");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		/**
		 * @param sender
		 * @param args
		 */
		private void list(CommandSender sender, String[] args) {
			plugin.Msg(sender, "Current config of bot:");
			plugin.Msg(sender, "");
			for(int i = 0; i < flags.length; i++) {
				String flag = flags[i];
				plugin.Msg(sender, flag+": "+plugin.getConfig().get(flag));
			}
			
		}

		/**
		 * @param sender
		 * @param args
		 */
		private void save(CommandSender sender, String[] args) {
			try {
				plugin.getConfig().save("plugins/AmpleChatBot/config.yml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		/**
		 * @param sender
		 * @param args
		 */
		private void set(CommandSender sender, String[] args) {
			if(args.length > 3) {
				for(int i = 0; i < flags.length; i++) {
					String flag = flags[i];
					if(flag.contains(args[2])) {
						plugin.getConfig().set(args[2], args[3]);
						plugin.Msg(sender, "\""+args[2]+"\" was set to \""+args[3]+"\"");
					}
					else if(i >= flags.length) {
						flagsAvailible(sender);
					}
				}
			
			} else {
				flagsAvailible(sender);
			}
		}

		private void flagsAvailible(CommandSender sender) {
			String dflag = "";
			for(int u = 0; u < flags.length; u++) {
				dflag += flags[u]+", ";
				
			}
			plugin.Msg(sender, "Flags Avaliable: "+dflag);
			
		}
	}
	class Db {
		public void cmd(CommandSender sender, String[] args) {
			if(args.length >= 2) {
				if(args[1].equalsIgnoreCase("export")) {
					exportDb(sender);
				} else if(args[1].equalsIgnoreCase("import")) {
					try {
						importDb(sender, args);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			} else {
				plugin.Msg(sender, "This command exports and imports the database!");
				plugin.Msg(sender, "");
				plugin.Msg(sender, "/ample db export");
				plugin.Msg(sender, "/ample db import");
			}
		}

		private void importDb(CommandSender sender, String[] args) throws IOException {
			File File = new File("plugins/AmpleChatBot/db.yml");
			if( File.exists() ) {	
				InputStream input = new FileInputStream(new File(
						"plugins/AmpleChatBot/db.yml"));
				Yaml yaml = new Yaml();
				for (Object data : yaml.loadAll(input)) {
					plugin.Msg(sender, data.toString());
				}
				input.close();
			}
			
		}

		private void exportDb(CommandSender sender) {
			
		}
	}
}
