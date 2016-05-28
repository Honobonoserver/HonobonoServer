package com.github.syuchan1005.honobonoserver.listener;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import com.github.syuchan1005.honobonoserver.wrapper.PermissionsExWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by syuch1an on 2016/05/22.
 */
@RegistManager.AddListener
public class ChatFormatListener implements Listener {
	private HonobonoServer plugin;

	public ChatFormatListener(HonobonoServer plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		String format = formatText(plugin.getConfigFile().getString("ChatFormat"), event.getMessage(), event.getPlayer());
		event.setFormat(format);
	}

	public static String formatText(String format, String message, Player player) {
		format = format.replaceAll("<message>", message);
		format = format.replaceAll("<player>" , player.getDisplayName());
		format = format.replaceAll("<prefix>" , PermissionsExWrapper.getPrefix(player));
		format = format.replaceAll("<suffix>" , PermissionsExWrapper.getSuffix(player));
		format = format.replaceAll("<world>"  , player.getWorld().getName());
		return format;
	}
}
