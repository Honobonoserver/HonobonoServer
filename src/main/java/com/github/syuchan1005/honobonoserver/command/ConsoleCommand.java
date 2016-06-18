package com.github.syuchan1005.honobonoserver.command;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by syuchan on 2016/06/04.
 */
public class ConsoleCommand {
	private HonobonoServer plugin;

	public ConsoleCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}
	// plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), );

	@RegistManager.AddCommand(Command = "hn", subCommand = "console", Permission = "hns.c.console")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		String c = "";
		for (int i = 1; i < args.length; i++) c += args[i] + " ";
		plugin.getLogger().info(c);
		plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), c);
		return true;
	}
}
