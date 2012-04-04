package org.jaggy.bukkit.ample.cmds;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jaggy.bukkit.ample.Ample;
import org.jaggy.bukkit.ample.config.Config;
import org.jaggy.bukkit.ample.db.DB;

public class cmdAmpq implements CommandExecutor {
	
	private Ample plugin = new Ample();
	private DB db;
	private Config config;
	
	public cmdAmpq(Ample instance) {
		plugin = instance;
		db = plugin.getDB();
		config = plugin.getDConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		String question = "";
		
		for(int i = 0; i < args.length; i++) {
			question += args[i];
			question += " ";
		}
		question = question.trim();
		try {
			addQuestion(sender, question);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			db.Error("DB error:" +e);
		}
		return true;
	}

	private void addQuestion(CommandSender sender, String question) throws SQLException {
		db.query("INSERT INTO "+config.getDbPrefix()+"Responses (keyphrase,response) VALUES ('"+question+"','');");
		if(db.lastUpdate == 0) 
			plugin.Msg(sender, "Db error: Failed to add the question.");
		else 
			plugin.Msg(sender, "Question ID: "+db.lastUpdate);
	}
	
}
