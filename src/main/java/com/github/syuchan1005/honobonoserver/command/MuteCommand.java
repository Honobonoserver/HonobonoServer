package com.github.syuchan1005.honobonoserver.command;

import java.util.ArrayList;
import java.util.List;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand {
	public static List<Player> Players = new ArrayList<>();
	private HonobonoServer plugin;

	public MuteCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@RegistManager.AddCommand(Command = "hn", subCommand = "mute", Aliases = "m")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		if(args.length != 2) {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.warning.arguments"));
			return true;
		}
		Player player = Bukkit.getPlayer(args[1]);
		if(player == null) {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.warning.invalidname"));
			return true;
		}
		if(Players.contains(player)) {
			sender.sendMessage(this.plugin.getLanguages().getString(player, "honobonoserver.mute.remove").replaceAll("<Target>", player.getDisplayName()));
			Players.remove(player);
		} else {
			sender.sendMessage(this.plugin.getLanguages().getString(player, "honobonoserver.mute.add").replaceAll("<Target>", player.getDisplayName()));
			Players.add(player);
		}
		return true;
	}
}
