package com.github.syuchan1005.honobonoserver.command;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by syuchan on 2016/05/30.
 */
public class TPCommand {
	private HonobonoServer plugin;

	public TPCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@RegistManager.AddCommand(Command = "hn", subCommand = "tp", Permission = "hns.c.tp")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		if(!(sender instanceof Player) || args.length <= 1) return false;
		Location loc = Bukkit.getWorld(args[1]).getSpawnLocation();
		switch (args.length) {
			case 7:
				loc.setYaw(Float.parseFloat(args[6]));
			case 6:
				loc.setPitch(Float.parseFloat(args[5]));
			case 5:
				loc.setZ(Double.parseDouble(args[4]));
			case 4:
				loc.setY(Double.parseDouble(args[3]));
			case 3:
				loc.setX(Double.parseDouble(args[2]));
		}
		((Player) sender).teleport(loc);
		return true;
	}
}
