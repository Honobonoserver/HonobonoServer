package com.github.syuchan1005.honobonoserver.runnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class WitherMoveRunnable extends BukkitRunnable {
	public static List<World> worlds = Bukkit.getWorlds().stream()
			.filter(w -> w.getEnvironment() == Environment.NETHER).collect(Collectors.toList());

	public static List<PotionEffect> curse = new ArrayList<>(); {
		curse.add(new PotionEffect(PotionEffectType.BLINDNESS, 5, 100));
		curse.add(new PotionEffect(PotionEffectType.SLOW, 5, 100));
		curse.add(new PotionEffect(PotionEffectType.WITHER, 5, 100));
		curse.add(new PotionEffect(PotionEffectType.JUMP, 5, -100));
	}

	@Override
	public void run() {
		for (World world : worlds) {
			for (Entity e : world.getEntitiesByClass(Wither.class)) {
				Location loc = e.getLocation();
				int X = loc.getBlockX();
				int Y = loc.getBlockY();
				int Z = loc.getBlockZ();
				int radius = 4;
				int radiusSquared = radius * radius;
				for (int x = X - radius; x <= X + radius; x++) {
					for (int y = Y - radius; y <= Y + radius; y++) {
						for (int z = Z - radius; z <= Z + radius; z++) {
							if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) <= radiusSquared) {
								world.playEffect(new Location(world, x, y, z), Effect.PORTAL, 10);
							}
						}
					}
				}
				// 呪い
				world.getNearbyEntities(loc, 3, 3, 3).stream().filter(player -> player instanceof Player).forEach(player -> ((Player) player).addPotionEffects(curse));
			}
		}
	}

}
