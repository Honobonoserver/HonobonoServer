package com.github.syuchan1005.honobonoserver.listener;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import com.github.syuchan1005.honobonoserver.util.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by syuchan on 2016/05/08.
 */

@RegistManager.AddListener
public class PassWordSignListener implements Listener {
	private static HonobonoServer plugin;
	private static Map<String, String> config;

	public PassWordSignListener (HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		String pass = isPassWordSign(event.getClickedBlock());
		if (pass == null) return;
		AnvilGUI anvilGUI = createAnvilGUI(event.getPlayer(), pass, event.getClickedBlock());
		try {
			anvilGUI.open();
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			event.getPlayer().sendMessage(plugin.getLanguages().getString(event.getPlayer(), "honobonoserver.passwordsign.openerror"));
		}
	}

	public static String isPassWordSign(Block block) {
		if(block.getType() != Material.WALL_SIGN) return null;
		Sign sign = (Sign)block.getState();
		if(!sign.getLine(0).equalsIgnoreCase("[PassWord]")) return null;
		if(config == null) {
			config = new HashMap<>();
			for (String passWordSign : plugin.getConfigFile().getStringList("PassWordSign")) {
				String[] sp = passWordSign.split(":");
				config.put(sp[0], sp[1]);
			}
		}
		if(!config.containsKey(sign.getLine(1))) return null;
		return config.get(sign.getLine(1));
	}

	public AnvilGUI createAnvilGUI(Player player, String pass, Block block) {
		int lv = player.getLevel();
		AnvilGUI anvilGUI = new AnvilGUI(plugin, player, new AnvilGUI.AnvilClickEventHandler(){
			@Override
			public void onAnvilClick(AnvilGUI.AnvilClickEvent event){
				if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT &&
						event.getName().length() != 0){
					event.setWillClose(true);
					event.setWillDestroy(true);
					doing(event.getName(), pass, block, player);
				} else {
					boolean b = event.getSlot() == AnvilGUI.AnvilSlot.INPUT_RIGHT;
					event.setWillClose(b);
					event.setWillDestroy(b);
				}
				player.setLevel(lv);
			}
		});
		ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
		itemStack.setDurability((short)5);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName("PassWord: ");
		itemStack.setItemMeta(itemMeta);
		anvilGUI.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, itemStack);
		return anvilGUI;
	}

	public void doing(String itemName, String pass, Block sign, Player player) {
		if(itemName.endsWith(pass)){
			int y = player.getLocation().getBlockY() - sign.getLocation().getBlockY();
			Location tp = sign.getLocation();
			tp.setYaw(player.getLocation().getYaw());
			tp.setPitch(player.getLocation().getPitch());
			switch (((org.bukkit.material.Sign)sign.getState().getData()).getFacing().name()) {
				case "WEST":
					player.teleport(tp.add(2.5, y, 0.5));
					break;
				case "EAST":
					player.teleport(tp.add(-1.5, y, 0.5));
					break;
				case "NORTH":
					player.teleport(tp.add(0.5, y, 2.5));
					break;
				case "SOUTH":
					player.teleport(tp.add(0.5, y, -1.5));
					break;
			}
		} else {
			player.sendMessage(plugin.getLanguages().getString(player, "honobonoserver.passwordsign.failed"));
		}
	}
}