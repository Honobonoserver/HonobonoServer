package co.honobono.honobonoserver.listener;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import co.honobono.honobonoserver.constructor.RegistManager.AddListener;


@AddListener
public class EnderMiteListener implements Listener {
	Random ran = new Random();

	@EventHandler
	public void SpawnBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getLocation().getWorld().getEnvironment() != Environment.THE_END)
			return;
		if (block.getType() == Material.ENDER_STONE || block.getType() == Material.OBSIDIAN) {
			if (ran.nextInt(4) != 1) return;
			block.getLocation().getWorld().spawnEntity(block.getLocation().add(0.5, 0, 0.5), EntityType.ENDERMITE);
		}
	}

	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		Entity e = event.getEntity();
		if(e.getLocation().getWorld().getEnvironment() == Environment.THE_END &&
				e.getType() == EntityType.ENDERMITE) {
			if(ran.nextInt(10) == 0)
			e.getLocation().getWorld().spawn(e.getLocation(), TNTPrimed.class);
		}
	}

}
