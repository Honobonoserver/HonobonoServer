package com.github.syuchan1005.honobonoserver.listener;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by syuchan on 2016/06/06.
 */
@RegistManager.AddListener
public class EndPortalListener implements Listener {
	private static HonobonoServer plugin;

	public EndPortalListener(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPortal(PlayerMoveEvent event) {
		Block block = event.getFrom().getBlock();
		if(block.getType() != Material.ENDER_PORTAL) return;
		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 800, 3));
		new BukkitRunnable() {
			@Override
			public void run() {
				event.getPlayer().teleport(Bukkit.getWorld(plugin.getConfigFile().getString("EndWorld")).getSpawnLocation());
			}
		}.runTaskLater(plugin, 801L);
	}
}
