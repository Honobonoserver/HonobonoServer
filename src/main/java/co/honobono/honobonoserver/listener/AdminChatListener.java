package co.honobono.honobonoserver.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import co.honobono.honobonoserver.command.AdminChatCommand;
import co.honobono.honobonoserver.constructor.RegistManager.AddListener;

@AddListener
public class AdminChatListener implements Listener{

	@EventHandler
	public void adminchat(AsyncPlayerChatEvent event) {
		if(AdminChatCommand.Players.contains(event.getPlayer())) {
			event.setCancelled(true);
			Bukkit.broadcast("<" + event.getPlayer().getDisplayName() + ">" + event.getMessage(), "honobonoserver.adminchat");
		}
	}

}
