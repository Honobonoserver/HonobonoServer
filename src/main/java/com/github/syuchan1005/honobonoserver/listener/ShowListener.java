package com.github.syuchan1005.honobonoserver.listener;

import com.github.syuchan1005.honobonoserver.command.ShowCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.syuchan1005.honobonoserver.constructor.RegistManager.AddListener;
import org.bukkit.event.player.PlayerQuitEvent;

@AddListener
public class ShowListener implements Listener {

	@EventHandler
	public void onJoinEvent(PlayerJoinEvent event) {
		ShowCommand.Players.forEach(p -> event.getPlayer().hidePlayer(p));
	}

	@EventHandler
	public void onQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(ShowCommand.Players.contains(player)) {
			ShowCommand.show(player);
		}
	}
}
