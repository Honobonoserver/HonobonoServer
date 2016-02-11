package co.honobono.honobonoserver.command;

import java.text.SimpleDateFormat;

import co.honobono.honobonoserver.HonobonoServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.honobono.honobonoserver.constructor.RegistManager.AddCommand;

public class LookCommand {
	private HonobonoServer plugin;

	public LookCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@AddCommand(Command = "hn", subCommand = "look", Aliases = "l")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		Player player = null;
		if (args.length == 1) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.warning.ingame"));
				return true;
			}
			player = (Player) sender;
		} else if (args.length == 2) {
			player = Bukkit.getPlayer(args[1]);
		}
		sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.status"));
		if (player == null) {
			OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.name"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.uuid"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.lastlogin"));
		} else {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.name"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.uuid"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.lastlogin"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.exp"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.health"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.flight"));
			Location loc = player.getLocation();
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.location.name"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.location.x"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.location.y"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.location.z"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.location.pitch"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.locaiton.yaw"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.hostname"));
		}
		return true;
	}

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public static String getTime(long Mills) {
		return format.format(Mills);
	}

}
