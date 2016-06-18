package com.github.syuchan1005.honobonoserver.command;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

/**
 * Created by syuchan on 2016/05/14.
 */
public class InfoCommand {
	private HonobonoServer plugin;

	public InfoCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@RegistManager.AddCommand (Command = "hn", subCommand = "info", Permission = "hns.c.info")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		if(!(sender instanceof Player) || args.length != 2) return false;
		String str;
		switch (args[1].toUpperCase()) {
			case "BLOCK":
				str = ((Player)sender).getTargetBlock(((Set<Material>) null), 200).toString();
				break;
			case "ITEM":
				ItemStack item = ((Player)sender).getInventory().getItemInMainHand();
				if(item == null) str = "null";
				else str = item.toString();
				break;
			default:
				str = "";
				break;
		}
		sender.sendMessage(str);
		return true;
	}
}
