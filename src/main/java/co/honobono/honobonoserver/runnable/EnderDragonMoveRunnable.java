package co.honobono.honobonoserver.runnable;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class EnderDragonMoveRunnable extends BukkitRunnable {
	public static List<World> worlds = Bukkit.getWorlds().stream()
			.filter(w -> w.getEnvironment() == Environment.THE_END).collect(Collectors.toList());

	@Override
	public void run() {
		for (World world : worlds) {
			for (Entity e : world.getEntitiesByClass(EnderDragon.class)) {
				// 地形破壊
				Location loc = e.getLocation();
				int X = loc.getBlockX();
				int Y = loc.getBlockY() + 3;
				int Z = loc.getBlockZ();
				int radius = 6;
				int radiusSquared = radius * radius;
				for (int x = X - radius; x <= X + radius; x++) {
					for (int y = Y - radius + 1; y <= Y + radius; y++) {
						for (int z = Z - radius; z <= Z + radius; z++) {
							if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) <= radiusSquared) {
								Block b = new Location(world, x, y, z).getBlock();
								if (b.getType() != Material.BEDROCK) {
									b.setType(Material.AIR);
								}
							}
						}
					}
				}
				Damageable d = (Damageable) e;
				if (d.getMaxHealth() == 200.0D) {
					if (e.getMetadata("phase2").size() >= 1 && e.getMetadata("phase2").get(0).asBoolean()) {
						e.setVelocity(e.getVelocity().normalize().multiply(1.5));
						d.setMaxHealth(100.0D);
						// d.setHealth(100.0D);
					} else {
						d.setMaxHealth(600.0D);
						d.setHealth(600.0D);
					}
				}
			}
		}
	}
}