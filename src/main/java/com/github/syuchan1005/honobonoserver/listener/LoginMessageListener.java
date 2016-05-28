package com.github.syuchan1005.honobonoserver.listener;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.syuchan1005.honobonoserver.constructor.RegistManager.AddListener;

@AddListener
public class LoginMessageListener implements Listener {
	private HonobonoServer plugin;

	public LoginMessageListener(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void LoginMsg(PlayerJoinEvent event) {
		event.setJoinMessage("");
		Player player = event.getPlayer();
		Bukkit.getOnlinePlayers().stream().forEach(p -> {
			p.sendMessage(this.plugin.getLanguages().getString(p, "honobonoserver.loginmessage.joinmessage").replaceAll("<Player>", player.getDisplayName()));
			if(!player.hasPlayedBefore())p.sendMessage(this.plugin.getLanguages().getStringList(p, "honobonoserver.loginmessage.firstjoin")
					.stream().map(s -> s.replaceAll("<Player>", player.getDisplayName())).toArray(String[]::new));
		});
	}

	@EventHandler
	public void LogoutMsg(PlayerQuitEvent event) {
		event.setQuitMessage("");
		Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(this.plugin.getLanguages().getString(player, "honobonoserver.loginmessage.quitmessage").replaceAll("<Player>", event.getPlayer().getDisplayName())));
	}
}
