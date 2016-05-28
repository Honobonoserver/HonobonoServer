package co.honobono.honobonoserver.wrapper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

/**
 * Created by syuchan on 2016/05/22.
 */
public class PermissionsExWrapper {
	private static Plugin permissionEx;
	private static Object PermissionsManager;
	private static Method M_getUser, M_getPrefix, M_getSuffix;

	public static Plugin getPermissionEx() {
		if (permissionEx == null) permissionEx = Bukkit.getPluginManager().getPlugin("PermissionsEx");
		return permissionEx;
	}

	public static Object getPermissionsManager() {
		if (getPermissionEx() == null) return null;
		try {
			if (PermissionsManager == null) {
				Method M_getPermissionsManager = permissionEx.getClass().getMethod("getPermissionsManager");
				PermissionsManager = M_getPermissionsManager.invoke(getPermissionEx());
			}
			return PermissionsManager;
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}

	public static Object getPermissionUser(Player player) {
		if (getPermissionsManager() == null) return null;
		try {
			if (M_getUser == null) {
				M_getUser = getPermissionsManager().getClass().getMethod("getUser", Player.class);
			}
			return M_getUser.invoke(getPermissionsManager(), player);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}

	public static String getPrefix(Player player) {
		try {
			Object user = getPermissionUser(player);
			if (user == null) return "";
			if (M_getPrefix == null) {
				M_getPrefix = user.getClass().getMethod("getPrefix");
			}
			return (String) M_getPrefix.invoke(user);
		} catch (ReflectiveOperationException e) {
			return "";
		}
	}

	public static String getSuffix(Player player) {
		try {
			Object user = getPermissionUser(player);
			if (user == null) return "";
			if (M_getSuffix == null) {
				M_getSuffix = user.getClass().getMethod("getSuffix");
			}
			return (String) M_getSuffix.invoke(user);
		} catch (ReflectiveOperationException e) {
			return "";
		}
	}
}
