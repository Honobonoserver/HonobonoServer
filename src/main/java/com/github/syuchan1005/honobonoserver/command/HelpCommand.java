package com.github.syuchan1005.honobonoserver.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager.AddCommand;

public class HelpCommand {
	HonobonoServer plugin;

	public HelpCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@AddCommand(Command = "hn")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.help.enter"));
		return true;
	}

	@AddCommand(Command = "hn", subCommand = "help")
	public boolean onCommand2(CommandSender sender, Command cmd, String[] args) {
		this.plugin.getManager().sendHelpMessage(sender, "hn");
		return true;
	}

}
