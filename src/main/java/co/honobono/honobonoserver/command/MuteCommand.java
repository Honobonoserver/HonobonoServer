package co.honobono.honobonoserver.command;

import java.util.ArrayList;
import java.util.List;

import co.honobono.honobonoserver.HonobonoServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.honobono.honobonoserver.constructor.RegistManager.AddCommand;

public class MuteCommand {
	public static List<Player> Players = new ArrayList<>();
	private HonobonoServer plugin;

	public MuteCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@AddCommand(Command = "hn", subCommand = "mute", Aliases = "m")
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
			sender.sendMessage(this.plugin.getLanguages().getString(player, "honobonoserver.mute.remove"));
			Players.remove(player);
		} else {
			sender.sendMessage(this.plugin.getLanguages().getString(player, "honobonoserver.mute.add"));
			Players.add(player);
		}
		return true;
	}
}
