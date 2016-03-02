package co.honobono.honobonoserver.command;

import java.text.SimpleDateFormat;

import co.honobono.honobonoserver.HonobonoServer;
import co.honobono.honobonoserver.constructor.LanguageHelper;
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
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.name").replaceAll("<Name>", p.getName()));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.uuid").replaceAll("<Uuid>", p.getUniqueId().toString()));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.lastlogin").replaceAll("<Time>", getTime(p.getLastPlayed())));
		} else {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.name").replaceAll("<Name>", player.getDisplayName()));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.uuid").replaceAll("<Uuid>", player.getUniqueId().toString()));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.lastlogin").replaceAll("<Time>", getTime(player.getLastPlayed())));
			try {
				sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.locale").replaceAll("<Locale>", LanguageHelper.getLocale(player)));
			} catch (ReflectiveOperationException e) {
				sender.sendMessage(e.getMessage());
			}
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.exp").replaceAll("<Exp>", String.valueOf(player.getExp())));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.health").replaceAll("<Health>", String.valueOf(player.getHealth())).replaceAll("<MaxHealth>", String.valueOf(player.getMaxHealth())));
			Location loc = player.getLocation();
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.location.name"));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.location.world").replaceAll("<World>", String.valueOf(loc.getWorld())));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.location.x").replaceAll("<X>", String.valueOf(loc.getX())));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.location.y").replaceAll("<Y>", String.valueOf(loc.getY())));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.location.z").replaceAll("<Z>", String.valueOf(loc.getZ())));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.location.pitch").replaceAll("<Pitch>", String.valueOf(loc.getPitch())));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.location.yaw").replaceAll("<Yaw>", String.valueOf(loc.getYaw())));
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.look.hostname").replaceAll("<Name>", player.getAddress().getHostName()));
		}
		return true;
	}

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public static String getTime(long Mills) {
		return format.format(Mills);
	}

}
