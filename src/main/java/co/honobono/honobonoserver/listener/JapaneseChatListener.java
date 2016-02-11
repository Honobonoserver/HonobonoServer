package co.honobono.honobonoserver.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.ucchyocean.lc.japanize.IMEConverter;
import com.github.ucchyocean.lc.japanize.KanaConverter;

import co.honobono.honobonoserver.constructor.RegistManager.AddListener;

@AddListener
public class JapaneseChatListener implements Listener {

	@EventHandler
	public void onASyncPlayerChat(AsyncPlayerChatEvent event) {
		String msg = event.getMessage();
		event.setMessage(IMEConverter.convByGoogleIME(KanaConverter.conv(msg)) + (hasZenkaku(msg) ? "" : "(" + msg + ")"));
	}

	public static boolean hasZenkaku(String text) {
		return text.length() != text.getBytes().length;
	}
}
