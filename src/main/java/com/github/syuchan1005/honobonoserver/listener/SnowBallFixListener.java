package com.github.syuchan1005.honobonoserver.listener;

import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

/**
 * Created by syuchan on 2016/06/10.
 */
@RegistManager.AddListener
public class SnowBallFixListener implements Listener {

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player || e.getDamager() instanceof Snowball)) return;
		Player player = (Player) e.getEntity();
		Snowball s = (Snowball) e.getDamager();
		if (!(s.getShooter() instanceof Player)) return;
		Player shooter = (Player) s.getShooter();
		if (player.equals(shooter)) {
			e.setCancelled(true);
			player.setVelocity(new Vector(0, 0, 0));
		}
	}
}
