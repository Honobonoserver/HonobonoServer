package com.github.syuchan1005.honobonoserver.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.syuchan1005.honobonoserver.command.MuteCommand;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager.AddListener;

@AddListener
public class MuteListener implements Listener{

	@EventHandler
	public void Mute(AsyncPlayerChatEvent event) {
		if(MuteCommand.Players.contains(event.getPlayer())) {
			Bukkit.broadcast("<" + event.getPlayer().getDisplayName() + "> " + event.getMessage(), "honobonoserver.mute");
			event.setCancelled(true);
		}
	}
}
