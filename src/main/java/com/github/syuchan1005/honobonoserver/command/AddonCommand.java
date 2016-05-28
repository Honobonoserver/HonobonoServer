package com.github.syuchan1005.honobonoserver.command;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.File;

/**
 * Created by syuchan on 2016/05/22.
 */
public class AddonCommand {
	private HonobonoServer plugin;

	public AddonCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@RegistManager.AddCommand(Command = "hn", subCommand = "addon")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		for(File file : plugin.getManager().getAddonFile()) {
			sender.sendMessage(file.getName());
		}
		return true;
	}
}
