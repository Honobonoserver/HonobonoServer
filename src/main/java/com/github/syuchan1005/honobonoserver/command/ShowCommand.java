package com.github.syuchan1005.honobonoserver.command;

import java.util.ArrayList;
import java.util.List;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.wrapper.DynmapAPIWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.syuchan1005.honobonoserver.constructor.RegistManager.AddCommand;

public class ShowCommand {
	public static List<Player> Players = new ArrayList<>();
	private HonobonoServer plugin;

	public ShowCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}
	
	@AddCommand(Command = "hn", subCommand = "show", Aliases = "s", Permission = "hns.c.show")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.warning.ingame"));
			return true;
		}
		Player player = (Player) sender;
		if(Players.contains(player)) {
			sender.sendMessage(this.plugin.getLanguages().getString(player, "honobonoserver.show.remove"));
			show(player);
		} else {
			sender.sendMessage(this.plugin.getLanguages().getString(player, "honobonoserver.show.add"));
			hide(player);
		}
		return true;
	}

	public static void show(Player player) {
		Players.remove(player);
		Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(player));
		DynmapAPIWrapper.setPlayerVisiblity(player, true);
	}

	public static void hide(Player player) {
		Players.add(player);
		Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(player));
		DynmapAPIWrapper.setPlayerVisiblity(player, false);
	}
}
