package co.honobono.honobonoserver.listener;

import java.util.List;
import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import co.honobono.honobonoserver.HonobonoServer;
import co.honobono.honobonoserver.constructor.RegistManager.AddListener;
import co.honobono.honobonoserver.util.Other;

@AddListener
public class ChangeMotdListener implements Listener{

	@EventHandler(ignoreCancelled = true)
	public void onServerListPing(ServerListPingEvent event){
		List<String> Motd = HonobonoServer.getConfigFile().getStringList("Motd");
		String motd = Motd.get(new Random().nextInt(Motd.size()));
		if (!(motd.length() == 0)) event.setMotd(Other.color(motd, null));
	}

}
