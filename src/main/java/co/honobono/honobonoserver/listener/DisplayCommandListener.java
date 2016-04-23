package co.honobono.honobonoserver.listener;

import co.honobono.honobonoserver.HonobonoServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import co.honobono.honobonoserver.constructor.RegistManager.AddListener;

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
	}

}
