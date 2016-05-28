package com.github.syuchan1005.honobonoserver.listener;

import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.github.syuchan1005.honobonoserver.command.FreezeCommand;

@RegistManager.AddListener
public class FreezeListener implements Listener{

	@EventHandler
	public void moveoff(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (FreezeCommand.Players.contains(player)) {
			Location from = event.getFrom();
			Location to = event.getTo();
			if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) return;
			player.teleport(from);
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void dropoff(PlayerDropItemEvent event) {
		if (FreezeCommand.Players.contains(event.getPlayer())) event.setCancelled(true);
	}

	@EventHandler
	public void pickupoff(PlayerPickupItemEvent event) {
		if (FreezeCommand.Players.contains(event.getPlayer())) event.setCancelled(true);
	}

	@EventHandler
	public void interactoff(PlayerInteractEvent event) {
		if (FreezeCommand.Players.contains(event.getPlayer())) event.setCancelled(true);
	}

	@EventHandler
	public void EntityDamageEvent(EntityDamageEvent event) {
		if(!(event.getEntityType() == EntityType.PLAYER)) return;
		if(FreezeCommand.Players.contains((Player)event.getEntity())) event.setCancelled(true);
	}
}
