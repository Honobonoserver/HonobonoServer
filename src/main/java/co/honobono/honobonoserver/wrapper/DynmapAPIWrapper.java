package co.honobono.honobonoserver.wrapper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

/**
 * Created by syuchan on 2016/05/22.
 */
public class DynmapAPIWrapper {
	private static Plugin dynmap;
	private static Method M_setPlayerVisiblity;

	public static Plugin getDynmap() {
		if(dynmap == null) dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
		return dynmap;
	}

	public static void setPlayerVisiblity(Player player, boolean selected) {
		try {
			if (dynmap == null) return;
			if (M_setPlayerVisiblity == null) {
				M_setPlayerVisiblity = dynmap.getClass().getMethod("setPlayerVisiblity", Player.class, boolean.class);
			}
			M_setPlayerVisiblity.invoke(dynmap, player, selected);
		} catch (ReflectiveOperationException e) {
			return;
		}
	}
}
