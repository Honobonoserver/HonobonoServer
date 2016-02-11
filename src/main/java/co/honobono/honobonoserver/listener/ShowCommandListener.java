package co.honobono.honobonoserver.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import co.honobono.honobonoserver.constructor.RegistManager.AddListener;

@AddListener
public class ShowCommandListener implements Listener{

	@EventHandler
	public void sendCommand(PlayerCommandPreprocessEvent event) {
		String format = "<ShowCommand> Use: " + event.getPlayer() + " | " + event.getMessage();
		Bukkit.broadcast(format, "honobonoserver.showcommand");
	}

}
