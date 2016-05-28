package com.github.syuchan1005.honobonoserver.command;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager.AddCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by syuchan on 2016/05/07.
 */
public class GetCommand {
	private HonobonoServer plugin;

	public GetCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@AddCommand(Command = "hn", subCommand = "get")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Dont Player");
			return true;
		}
		if(!(args.length <= 3 && args.length >= 2)) {
			sender.sendMessage("Invocation Args");
			return true;
		}
		String name = args[1] + ":";
		ItemStack itemStack = new ItemStack(Material.BEDROCK);
		if(args.length == 3) {
			name += args[2];
			itemStack.setDurability(Short.parseShort(args[2]));
		} else {
			name += 0;
		}
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(name);
		itemStack.setItemMeta(itemMeta);
		((Player)sender).getInventory().addItem(itemStack);
		return true;
	}
}
