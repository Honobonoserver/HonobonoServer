package co.honobono.honobonoserver.listener;

import co.honobono.honobonoserver.HonobonoServer;
import co.honobono.honobonoserver.util.Other;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import co.honobono.honobonoserver.constructor.RegistManager.AddListener;

import java.util.List;
import java.util.Map;

@AddListener
public class DisplayCommandListener implements Listener {
	private HonobonoServer plugin;

	public DisplayCommandListener(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void sendCommand(PlayerCommandPreprocessEvent event) {
		Bukkit.broadcast(ChatColor.GRAY + this.plugin.getLanguages().getString(event.getPlayer(), "honobonoserver.display.format")
				.replaceAll("<Player>", event.getPlayer().getDisplayName())
				.replaceAll("<Command>", event.getMessage()), "honobonoserver.showcommand");
		String[] split = event.getMessage().trim().split(" ");
		if(!plugin.getConfigFile().getBoolean("OverridePluginCommand.Enable")|| split.length == 0 ||
				!Other.include(split[0].toLowerCase(), "/plugins", "/pl", "/bukkit:plugins")) return;
		event.getPlayer().sendMessage(plugin.getConfigFile().getString("OverridePluginCommand.Content"));
		event.setCancelled(true);
	}

}
