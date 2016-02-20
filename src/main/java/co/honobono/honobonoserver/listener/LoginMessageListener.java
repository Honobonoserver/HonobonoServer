package co.honobono.honobonoserver.listener;

import co.honobono.honobonoserver.HonobonoServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import co.honobono.honobonoserver.constructor.RegistManager.AddListener;

@AddListener
public class LoginMessageListener implements Listener {
	private HonobonoServer plugin;

	public LoginMessageListener(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void LoginMsg(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		StringBuilder sb = new StringBuilder();
		sb.append(this.plugin.getLanguages().getString(event.getPlayer(), "honobonoserver.loginmessage.joinmessage").replaceAll("<Player>", player.getDisplayName()));
		if (!(player.hasPlayedBefore())) {
			sb.append("\n");
			for (String first : this.plugin.getLanguages().getStringList(player, "honobonoserver.loginmessage.firstjoin")) {
				Bukkit.broadcastMessage(first.replaceAll("<Player>", player.getDisplayName()));
				sb.append("\n");
			}
		}
		event.setJoinMessage(sb.toString());
		player.sendMessage(this.plugin.getLanguages().getStringList(player, "honobonoserver.loginmessage.joinplayer").stream().map(s -> s.replaceAll("<Player>", event.getPlayer().getDisplayName())).toArray(String[]::new));
	}

	@EventHandler
	public void LogoutMsg(PlayerQuitEvent event) {
		event.setQuitMessage(this.plugin.getLanguages().getString(event.getPlayer(), "honobonoserver.loginmessage.quitmessage").replaceAll("<Player>", event.getPlayer().getDisplayName()));
	}
}
