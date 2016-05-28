package com.github.syuchan1005.honobonoserver.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.syuchan1005.honobonoserver.command.AdminChatCommand;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager.AddListener;

@AddListener
public class AdminChatListener implements Listener{

	@EventHandler
	public void adminchat(AsyncPlayerChatEvent event) {
		if(AdminChatCommand.Players.contains(event.getPlayer())) {
			event.setCancelled(true);
			Bukkit.broadcast(ChatColor.AQUA + "<" + event.getPlayer().getDisplayName() + ">" + event.getMessage(), "honobonoserver.adminchat");
		}
	}

}
