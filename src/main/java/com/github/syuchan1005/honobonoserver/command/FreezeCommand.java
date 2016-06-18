package com.github.syuchan1005.honobonoserver.command;

import java.util.ArrayList;
import java.util.List;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand {
	public static List<Player> Players = new ArrayList<>();
	private HonobonoServer plugin;

	public FreezeCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@RegistManager.AddCommand(Command = "hn", subCommand = "freeze", Aliases = "f", Permission = "hns.c.freeze")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		if(args.length != 2) {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.warning.arguments"));
			return true;
		}
		Player target = Bukkit.getPlayer(args[1]);
		if(Players.contains(target)) {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.freeze.remove").replaceAll("<Target>", target.getDisplayName()));
			Players.remove(target);
		} else {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.freeze.add").replaceAll("<Target>", target.getDisplayName()));
			Players.add(target);
		}
		return true;
	}
}
