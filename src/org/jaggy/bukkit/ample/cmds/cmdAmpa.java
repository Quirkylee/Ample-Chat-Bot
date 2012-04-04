package org.jaggy.bukkit.ample.cmds;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jaggy.bukkit.ample.Ample;
import org.jaggy.bukkit.ample.config.Config;
import org.jaggy.bukkit.ample.db.DB;


public class cmdAmpa implements CommandExecutor {
	
	private Ample plugin = new Ample();
	private DB db;
	private Config config;
	
	public cmdAmpa(Ample instance) {
		plugin = instance;
		db = plugin.getDB();
		config = plugin.getDConfig();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		String answer = "";
		
		for(int i = 1; i < args.length; i++) answer += args[i];
		if(!(sender instanceof Player)){
			try {
				setAnswer(sender, Integer.parseInt(args[0]), answer);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				setAnswer(sender, Integer.parseInt(args[0]), answer);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	/**
	 * Sets the answer to a question ID.
	 * 
	 * @param sender
	 * @param QID
	 * @param answer
	 * @throws SQLException
	 */
	public void setAnswer(CommandSender sender, Integer QID, String answer) throws SQLException {
			ResultSet result = db.query("SELECT * FROM "+config.getDbPrefix()+"Responses WHERE id = '"+QID+"';");
			if(result != null) {
				db.query("UPDATE "+config.getDbPrefix()+"Responses SET response = '"+answer+"' WHERE id = '"+QID+"';");
			}
			else {
				plugin.Msg(sender, "Unable to find the id: "+QID);
			}
	}
}
