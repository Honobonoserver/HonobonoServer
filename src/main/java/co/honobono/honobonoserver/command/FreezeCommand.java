package co.honobono.honobonoserver.command;

import java.util.ArrayList;
import java.util.List;

import co.honobono.honobonoserver.HonobonoServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.honobono.honobonoserver.constructor.RegistManager.AddCommand;

public class FreezeCommand {
	public static List<Player> Players = new ArrayList<>();
	private HonobonoServer plugin;

	public FreezeCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@AddCommand(Command = "hn", subCommand = "freeze", Aliases = "f")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		if(args.length != 2) {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.warning.arguments"));
			return true;
		}
		Player target = Bukkit.getPlayer(args[1]);
		if(Players.contains(target)) {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.freeze.remove"));
			Players.remove(target);
		} else {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.freeze.add"));
			Players.add(target);
		}
		return true;
	}
}
