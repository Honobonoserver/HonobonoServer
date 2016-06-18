package com.github.syuchan1005.honobonoserver.listener;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import com.github.syuchan1005.honobonoserver.constructor.RegistManager.AddListener;


@AddListener
public class EnderCrystalListener implements Listener {
	private static int[] deg = {0, 45, 90, 135, 180, 225, 270, 315, 360};

	@EventHandler
	public void Explode(EntityDamageByEntityEvent event) {
		Entity ent = event.getEntity();
		if (ent.getType() == EntityType.ENDER_CRYSTAL) {
			Location loc = ent.getLocation();
			for (int j = 0; j < 24; j++) {
				loc.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT).setVelocity(new Vector(Math.cos(j * 15), 0.3, Math.sin(j * 15)));
			}
		}
	}
}
