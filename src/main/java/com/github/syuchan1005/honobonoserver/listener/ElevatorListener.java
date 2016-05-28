package com.github.syuchan1005.honobonoserver.listener;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager.AddListener;
import org.bukkit.scheduler.BukkitRunnable;


@AddListener
public class ElevatorListener implements Listener {
	private HonobonoServer plugin;
	private static Map<Material, Integer> blocks = null;

	public ElevatorListener(HonobonoServer plugin) {
		this.plugin = plugin;
		if(blocks != null) return;
		blocks = new HashMap<>();
		for (String a : plugin.getConfigFile().getStringList("Elevator")) {
			String[] b = a.split(":");
			blocks.put(Material.getMaterial(b[0]), Integer.valueOf(b[1]));
		}
	}

	@EventHandler
	public void elevatorup(PlayerStatisticIncrementEvent event) {
		if (event.getStatistic() == Statistic.JUMP) this.Process(event.getPlayer(), true);
	}

	@EventHandler
	public void elevatordown(PlayerToggleSneakEvent event) {
		if (event.isSneaking()) this.Process(event.getPlayer(), false);
	}

	private void Process(Player player, boolean up) {
		Location loc = player.getLocation().clone();
		Material m = loc.subtract(0, 1, 0).getBlock().getType();
		if (!blocks.containsKey(m)) return;
		int scope = blocks.get(m);
		for (int i = 0; i < scope; i++) {
			loc.setY(loc.getY() + (up ? 1 : -1));
			if (loc.getBlock().getType() == m
					&& loc.clone().add(0, 1, 0).getBlock().getType().isTransparent()
					&& loc.clone().add(0, 1, 0).getBlock().getType().isTransparent()) {
				new BukkitRunnable() {
					@Override
					public void run() {
						player.teleport(loc.add(0, 1, 0));
						player.playSound(loc, Sound.ENTITY_ENDERMEN_TELEPORT, 10, 1);
						loc.getWorld().playEffect(loc, Effect.PORTAL, 5);
					}
				}.runTaskLater(this.plugin, 3);
				break;
			}
		}
	}
}
