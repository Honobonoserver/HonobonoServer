package co.honobono.honobonoserver.listener;

import java.util.Random;

import org.bukkit.World.Environment;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import co.honobono.honobonoserver.constructor.RegistManager.AddListener;



@AddListener
public class EnderManListener implements Listener {
	Random ran = new Random();

	@EventHandler
	public void endermite(EntityDamageByEntityEvent event) {
		Entity enderman = event.getEntity();
		if (!(enderman.getLocation().getWorld().getEnvironment() == Environment.THE_END))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		if (enderman.getType() != EntityType.ENDERMAN)
			return;
		for (int i = 0; i <= ran.nextInt(3) + 1; i++)
			enderman.getLocation().getWorld().spawn(enderman.getLocation(), Endermite.class);
	}

	@EventHandler
	public void BLINDNESS(EntityTargetLivingEntityEvent event) {
		if (event.getEntity().getType() != EntityType.ENDERMAN || !(event.getTarget() instanceof Player)) return;
		if (event.getEntity().getLocation().getWorld().getEnvironment() != Environment.THE_END) return;
		((Player) event.getTarget()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
	}
}
