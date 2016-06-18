package com.github.syuchan1005.honobonoserver.listener;

import com.github.syuchan1005.honobonoserver.HonobonoServer;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import com.github.syuchan1005.honobonoserver.util.Other;
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
		event.setFormat(Other.color(format, event.getPlayer()));
	}

	public static String formatText(String format, String message, Player player) {
		return format.replaceAll("<message>", message)
				.replaceAll("<player>" , player.getDisplayName())
				.replaceAll("<prefix>" , PermissionsExWrapper.getPrefix(player))
				.replaceAll("<suffix>" , PermissionsExWrapper.getSuffix(player))
				.replaceAll("<world>"  , player.getWorld().getName());
	}
}
