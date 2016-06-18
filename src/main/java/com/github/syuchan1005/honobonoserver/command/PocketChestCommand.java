package com.github.syuchan1005.honobonoserver.command;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager.AddCommand;
import com.github.syuchan1005.honobonoserver.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by syuchan on 2016/02/20.
 */
public class PocketChestCommand {
	private HonobonoServer plugin;
	private static Map<String, Inventory> map = null;

	public PocketChestCommand(HonobonoServer plugin) {
		this.plugin = plugin;
		if(map == null) {
			map = new HashMap<>();
			List<Map<?, ?>> lmap = plugin.getInv().getMapList("Inventories");
			if(lmap.size() == 0) return;
			Map<String, String> a = (Map<String, String>) lmap.get(0);
			for(Map.Entry<String, String> e : a.entrySet()) {
				try {
					map.put(e.getKey(), InventoryUtil.deserialize(e.getValue()));
				} catch (IOException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@AddCommand(Command = "hn", subCommand = "pocketchest", Aliases = { "chest", "pocket" }, Permission = "hns.c.pocketchest")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.warning.ingame"));
			return true;
		}
		Player player = (Player) sender;
		String Uuid = player.getUniqueId().toString();
		if(!map.containsKey(Uuid)) map.put(Uuid, Bukkit.createInventory(null, 27));
		player.openInventory(map.get(Uuid));
		return true;
	}

	public static Map<String, Inventory> getMap() {
		return map;
	}
}
