package com.github.syuchan1005.honobonoserver.listener;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.SQLite;
import org.bukkit.scheduler.BukkitRunnable;


@RegistManager.AddListener
public class HomeListener implements Listener {
	private HonobonoServer plugin;

	public HomeListener(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void inHome(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getClickedBlock().getType() != Material.BED_BLOCK) return;
		Location bed = event.getClickedBlock().getLocation();
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					plugin.getSQLite().checkPut("Home", "PlayerUUID", event.getPlayer().getUniqueId().toString(), event.getPlayer().getName(),
							SQLite.getTimeStamp(), bed.getWorld().getName(), bed.getBlockX(), bed.getBlockY(), bed.getBlockZ());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				event.getPlayer().sendMessage(plugin.getLanguages().getString(event.getPlayer(), "honobonoserver.home.success"));
			}
		}.runTask(plugin);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Location loc = getHome(this.plugin, event.getPlayer());
		if (loc != null) {
			event.setRespawnLocation(loc);
		}
	}

	public static Location getHome(HonobonoServer plugin, Player player) {
		String UUID = player.getUniqueId().toString();
		try {
			ResultSet result = plugin.getSQLite().get("Home", "PlayerUUID", UUID, "Home-World", "Home-X", "Home-Y", "Home-Z");
			result.next();
			Location loc = new Location(Bukkit.getWorld(result.getString(1)), result.getInt(2), result.getInt(3),
					result.getInt(4));
			return loc;
		} catch (SQLException e) {
			return null;
		}
	}
}
