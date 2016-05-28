package co.honobono.honobonoserver.command;

import co.honobono.honobonoserver.HonobonoServer;
import co.honobono.honobonoserver.constructor.RegistManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by syuchan on 2016/05/22.
 */
public class SightSeeingCommand {
	private Plugin plugin;
	private static Map<String, List<Location>> map = new HashMap<>();

	public SightSeeingCommand(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@RegistManager.AddCommand (Command = "hn", subCommand = "sightseeing", Aliases = "ss")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		Player player = ((Player) sender);
		switch (args[1].toUpperCase()) {
			case "SEE":
				GameMode gameMode = player.getGameMode();
				Location location = player.getLocation();
				player.setGameMode(GameMode.SPECTATOR);
				for (int i = 0; i <= map.get(args[2]).size(); i++) {
					final int j = i;
					new BukkitRunnable() {
						@Override
						public void run() {
							if(map.get(args[2]).size() == j) {
								player.setGameMode(gameMode);
								player.teleport(location);
							} else {
								player.teleport(map.get(args[2]).get(j));
							}
						}
					}.runTaskLater(plugin, 100 * i);
				}
				break;
			case "ADD":
				map.get(args[2]).add(player.getLocation());
				break;
			case "CREATE":
				map.put(args[2], new ArrayList<>());
				break;
		}
		return true;
	}
}
