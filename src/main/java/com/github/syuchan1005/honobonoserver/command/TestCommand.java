package com.github.syuchan1005.honobonoserver.command;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by syuchan on 2016/05/04.
 */
public class TestCommand {
	private HonobonoServer plugin;

	public TestCommand(HonobonoServer honobonoServer) {
		this.plugin = honobonoServer;
	}

	@RegistManager.AddCommand (Command = "hn", subCommand = "test")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		// Test
		return true;
	}
}
