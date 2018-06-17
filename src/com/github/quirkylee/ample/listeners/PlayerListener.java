package com.github.quirkylee.ample.listeners;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.quirkylee.ample.Ample;

public class PlayerListener implements Listener {

	private Ample plugin;
	
	public PlayerListener(Ample plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("ample.update")) {
			if (!plugin.version.equals(plugin.newversion) && !plugin.version.contains("TEST") && !(plugin.newversion == null)) {
				player.sendMessage(ChatColor.GREEN+"There is a new update for Ample chat bot! Current version: "+plugin.version+" New version: "+plugin.newversion);
				player.sendMessage(ChatColor.GOLD+"About the update: "+plugin.verinfo);
			}
		}
	}
}
