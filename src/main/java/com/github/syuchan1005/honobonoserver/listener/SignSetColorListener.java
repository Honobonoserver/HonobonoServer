package com.github.syuchan1005.honobonoserver.listener;

import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import com.github.syuchan1005.honobonoserver.util.Other;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * Created by syuchan on 2016/05/29.
 */
@RegistManager.AddListener
public class SignSetColorListener implements Listener{

	@EventHandler
	public void onSignWrite(SignChangeEvent event) {
		for (int i = 0; i < 4; i++) event.setLine(i, Other.color(event.getLine(i), null));
	}
}
