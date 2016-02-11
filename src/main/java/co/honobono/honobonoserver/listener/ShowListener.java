package co.honobono.honobonoserver.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import co.honobono.honobonoserver.command.ShowCommand;
import co.honobono.honobonoserver.constructor.RegistManager.AddListener;

@AddListener
public class ShowListener implements Listener{

	@EventHandler
	public void onEvent(PlayerJoinEvent event) {
		ShowCommand.Players.forEach(p -> event.getPlayer().hidePlayer(p));
	}
}
