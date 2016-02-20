package co.honobono.honobonoserver.command;

import co.honobono.honobonoserver.HonobonoServer;
import co.honobono.honobonoserver.constructor.LanguageHelper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import co.honobono.honobonoserver.constructor.RegistManager.AddCommand;
import co.honobono.honobonoserver.listener.HomeListener;

public class HomeCommand {
	private HonobonoServer plugin;

	public HomeCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@AddCommand(Command = "hn", subCommand = "home", Aliases = "h")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.warning.ingame"));
			return true;
		}
		Player player =  (Player) sender;
		Location loc = HomeListener.getHome(this.plugin, player);
		if (loc == null) {
			sender.sendMessage(this.plugin.getLanguages().getString(sender, "honobonoserver.home.notexits"));
		} else {
			Location loc1 = player.getLocation();
			LanguageHelper langs = this.plugin.getLanguages();
			new BukkitRunnable(){
				int time = 10;
				int y = -1;
				@Override
				public void run() {
					if(y == -1) y = loc1.getBlockY();
					if(time == 10 || time <= 4) player.sendMessage(langs.getString(sender, "honobonoserver.home.resec").replaceAll("<Time>", String.valueOf(time)));
					time--;
					if(loc1.getBlockX() != player.getLocation().getBlockX()
							|| loc1.getBlockZ() != player.getLocation().getBlockZ()
							|| y + 1 < player.getLocation().getBlockY()
							|| y - 1 > player.getLocation().getBlockY()) {
						player.sendMessage(langs.getString(sender, "honobonoserver.home.move"));
						this.cancel();
					}
					if(time == -1) {
						player.teleport(loc);
						this.cancel();
					}
				}
			}.runTaskTimer(this.plugin, 0, 20);
		}
		return true;
	}
}
