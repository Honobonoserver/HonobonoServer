package co.honobono.honobonoserver.command;

import java.util.ArrayList;
import java.util.List;

import co.honobono.honobonoserver.HonobonoServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.honobono.honobonoserver.constructor.RegistManager.AddCommand;
import org.bukkit.plugin.Plugin;

public class AdminChatCommand {
	public static List<Player> Players = new ArrayList<>();
	private HonobonoServer plugin;

	public AdminChatCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@AddCommand(Command = "hn", subCommand = "adminchat", Aliases = "a")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.warning.ingame"));
			return true;
		}
		Player player = (Player) sender;
		if(Players.contains(player)) {
			sender.sendMessage(this.plugin.getLanguages().getString(player, "honobonoserver.adminchat.remove"));
			Players.remove(player);
		} else {
			sender.sendMessage(this.plugin.getLanguages().getString(player, "honobonoserver.adminchat.add"));
			Players.add(player);
		}
		return true;
	}

}
