package com.github.syuchan1005.honobonoserver.listener;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import com.github.syuchan1005.honobonoserver.util.Other;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by syuchan on 2016/05/07.
 */

@RegistManager.AddListener
public class GetListener implements Listener {
	private HonobonoServer plugin;
	private static Pattern pattern = Pattern.compile("(.*):(.*)");

	public GetListener(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (event.getBlockAgainst().hasMetadata("HNGET")) {
			event.getBlockAgainst().setMetadata("HNGET", new FixedMetadataValue(plugin, false));
			event.getBlockAgainst().removeMetadata("HNGET", plugin);
		}
		if (event.getItemInHand().getType() != Material.BEDROCK) return;
		if (event.getItemInHand().getItemMeta().getDisplayName() == null) return;
		Matcher matcher = pattern.matcher(event.getItemInHand().getItemMeta().getDisplayName());
		if (!matcher.find()) return;
		if (Other.isInt(matcher.group(1)) && Other.isInt(matcher.group(2))) {
			Block place = event.getBlockPlaced();
			place.setType(Material.getMaterial(Integer.parseInt(matcher.group(1))));
			place.setData(Byte.parseByte(matcher.group(2)));
			place.setMetadata("HNGET", new FixedMetadataValue(plugin, true));
		}
	}

	@EventHandler
	public void onPhysics(BlockPhysicsEvent event) {
		eventDo(event);
	}

	@EventHandler
	public void onBlockFrom(BlockFromToEvent event) {
		eventDo(event);
	}

	@EventHandler
	public void onGrow(BlockGrowEvent event) {
		eventDo(event);
	}

	@EventHandler
	public void onFade(BlockFadeEvent event) {
		eventDo(event);
	}

	@EventHandler
	public void onLeaves(LeavesDecayEvent event) {
		eventDo(event);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (event.getBlock().hasMetadata("HNGET")) {
			event.getBlock().setMetadata("HNGET", new FixedMetadataValue(plugin, false));
			event.getBlock().removeMetadata("HNGET", plugin);
		}
	}

	public static void eventDo(BlockEvent event) {
		List<MetadataValue> metadata = event.getBlock().getMetadata("HNGET");
		if (metadata.size() <= 0 || !metadata.get(0).asBoolean()) return;
		((Cancellable) event).setCancelled(true);
	}
}
