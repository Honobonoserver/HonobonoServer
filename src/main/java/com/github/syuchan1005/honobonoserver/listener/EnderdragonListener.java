package com.github.syuchan1005.honobonoserver.listener;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.github.syuchan1005.honobonoserver.constructor.RegistManager.AddListener;


@AddListener
public class EnderdragonListener implements Listener {
	private static Random ran = new Random();
	private Plugin plugin;

	public EnderdragonListener(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void Fireball(EntityTargetEvent event) {
		if (!(event.getEntity().getType() == EntityType.ENDER_DRAGON))
			return;
		if (!(event.getTarget() instanceof Player))
			return;
		Player player = (Player) event.getTarget();
		Location loc = player.getLocation();
		for (float i = 0; i < 360; i = (float) (i + 0.5)) {
			loc.getWorld().playEffect(new Location(loc.getWorld(), loc.getX() + Math.sin(Math.toRadians(i)) * 2,
					loc.getY(), loc.getZ() + Math.cos(Math.toRadians(i)) * 2), Effect.PORTAL, 10);
		}
		player.playSound(loc, Sound.ENTITY_ENDERDRAGON_HURT, 10, 10);
		new BukkitRunnable() {
			@Override
			public void run() {
				TNTPrimed tnt = ((TNTPrimed) loc.getWorld().spawn(loc, TNTPrimed.class));
				tnt.setFuseTicks(1);
				tnt.setMetadata("ENDTNT", new FixedMetadataValue(plugin, true));
				loc.subtract(3, 1, 3);
				new BukkitRunnable() {
					@Override
					public void run() {
						for (int x = 0; x <= 6; x++) {
							for (int z = 0; z <= 6; z++) {
								for (int y = 0; y <= 1; y++) {
									Block b = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y,
											loc.getZ() + z).getBlock();
									if (b.getType() == Material.AIR && ran.nextInt(2) == 1)
										b.setType(Material.FIRE);
								}
							}
						}
					}
				}.runTaskLater(plugin, 3L);
			}
		}.runTaskLater(this.plugin, 70L);
	}

	@EventHandler
	public void onBomberTntDamagePlayer(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof TNTPrimed) {
			if (!event.getDamager().getMetadata("ENDTNT").get(0).asBoolean())
				return;
			Player player = (Player) event.getEntity();
			if (player.getLocation().getWorld().getEnvironment() != Environment.THE_END)
				return;
			player.setHealth(player.getHealth() - 8);
		}
	}

	@EventHandler
	public void onRevive(EntityDamageEvent event) {
		if (!(event.getEntity().getMetadata("phase2").size() >= 1
				&& event.getEntity().getMetadata("phase2").get(0).asBoolean())) {
			if (event.getEntity().getType() != EntityType.ENDER_DRAGON)
				return;
			EnderDragon dragon = (EnderDragon) event.getEntity();
			if ((((Damageable) dragon).getHealth() - event.getFinalDamage()) > 0)
				return;
			dragon.playEffect(EntityEffect.DEATH);
			new BukkitRunnable() {
				@Override
				public void run() {
					dragon.remove();
					Entity e = dragon.getLocation().getWorld().spawnEntity(dragon.getLocation(),
							EntityType.ENDER_DRAGON);
					e.setVelocity(new Vector(0, 1, 0));
					e.setCustomName("AdvancedEnderdragon");
					e.setMetadata("phase2", new FixedMetadataValue(plugin, true));
				}
			}.runTaskLater(this.plugin, 200L);
		}
	}

	@EventHandler
	public void onChangeEXP(EntityDeathEvent event) {
		if (!(event.getEntity().getMetadata("phase2").size() >= 1
				&& event.getEntity().getMetadata("phase2").get(0).asBoolean())) {
			if (event.getEntity().getType() == EntityType.ENDER_DRAGON) {
				event.setDroppedExp(0);
			}
		}
	}
}