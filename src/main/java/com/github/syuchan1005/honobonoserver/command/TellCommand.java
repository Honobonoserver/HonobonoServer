package com.github.syuchan1005.honobonoserver.command;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by syuchan on 2016/05/08.
 */
public class TellCommand {
	private HonobonoServer plugin;

	public TellCommand(HonobonoServer plugin) {
		this.plugin= plugin;
	}

	@RegistManager.AddCommand(Command = "hn", subCommand = "tell", Aliases = {"t"}, Permission = "hns.c.tell")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		String msg = "";
		for (int i = 1; i < args.length; i++) {
			msg += args[i];
			msg += " ";
		}
		sender.sendMessage(msg);
		return true;
	}

}
