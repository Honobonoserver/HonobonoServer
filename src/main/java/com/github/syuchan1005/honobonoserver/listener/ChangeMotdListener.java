package com.github.syuchan1005.honobonoserver.listener;

import java.util.List;
import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager.AddListener;
import com.github.syuchan1005.honobonoserver.util.Other;

@AddListener
public class ChangeMotdListener implements Listener {
	private static Random random = new Random();
	private HonobonoServer plugin;

	public ChangeMotdListener(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void onServerListPing(ServerListPingEvent event){
		List<String> Motd = this.plugin.getConfigFile().getStringList("Motd");
		String motd = Motd.get(random.nextInt(Motd.size()));
		if (motd.length() != 0) event.setMotd(Other.color(motd, null));
	}

}
