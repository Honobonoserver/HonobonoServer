package co.honobono.honobonoserver.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import co.honobono.honobonoserver.command.MuteCommand;
import co.honobono.honobonoserver.constructor.RegistManager.AddListener;

@AddListener
public class MuteListener implements Listener{

	@EventHandler
	public void Mute(AsyncPlayerChatEvent event) {
		if(MuteCommand.Players.contains(event.getPlayer())) {
			Bukkit.broadcast("<" + event.getPlayer().getDisplayName() + "> " + event.getMessage(), "honobonoserver.mute");
			event.setCancelled(true);
		}
	}
}
