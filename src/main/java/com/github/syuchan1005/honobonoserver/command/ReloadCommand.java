package com.github.syuchan1005.honobonoserver.command;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

/**
 * Created by syuchan on 2016/06/03.
 */
public class ReloadCommand {
	private HonobonoServer plugin;

	public ReloadCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@RegistManager.AddCommand(Command = "hn", subCommand = "reload", Permission = "hns.c.reload")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		try {
			plugin.reloadConfigFile();
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.plugin.success"));
		} catch (IOException | InvalidConfigurationException e) {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.plugin.failed"));
			e.printStackTrace();
		}
		return true;
	}
}
