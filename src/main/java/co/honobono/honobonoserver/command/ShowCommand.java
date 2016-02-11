package co.honobono.honobonoserver.command;

import java.util.ArrayList;
import java.util.List;

import co.honobono.honobonoserver.HonobonoServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.honobono.honobonoserver.constructor.RegistManager.AddCommand;

public class ShowCommand {
	public static List<Player> Players = new ArrayList<>();
	private HonobonoServer plugin;

	public ShowCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@AddCommand(Command = "hn", subCommand = "show", Aliases = "s")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.warning.ingame"));
			return true;
		}
		Player player = (Player) sender;
		if(Players.contains(player)) {
			sender.sendMessage(this.plugin.getLanguages().getString(player, "honobonoserver.show.remove"));
			Players.remove(player);
			Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(player));
		} else {
			sender.sendMessage(this.plugin.getLanguages().getString(player, "honobonoserver.show.add"));
			Players.add(player);
			Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(player));
		}
		return true;
	}
}
