package com.github.syuchan1005.honobonoserver.wrapper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by syuchan on 2016/05/22.
 */
public class PermissionsExWrapper {
	private static Plugin permissionEx;
	private static Object PermissionManager;
	private static Method M_getGroups;
	private static Method M_getUser, M_getPrefix, M_getSuffix, M_setParents;

	public static Plugin getPermissionEx() {
		if (permissionEx == null) permissionEx = Bukkit.getPluginManager().getPlugin("PermissionsEx");
		return permissionEx;
	}

	public static Object getPermissionManager() {
		if (getPermissionEx() == null) return null;
		try {
			if (PermissionManager == null) {
				Method M_getPermissionsManager = permissionEx.getClass().getMethod("getPermissionsManager");
				PermissionManager = M_getPermissionsManager.invoke(getPermissionEx());
			}
			return PermissionManager;
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}

	private static Object getGroups(String groupName) {
		try {
			Object manager = getPermissionManager();
			if (manager == null) return null;
			if (M_getGroups == null) {
				M_getGroups = manager.getClass().getMethod("getGroups", String.class);
			}
			return M_getGroups.invoke(manager, groupName);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}

	public static Object getPermissionUser(Player player) {
		if (getPermissionManager() == null) return null;
		try {
			if (M_getUser == null) {
				M_getUser = getPermissionManager().getClass().getMethod("getUser", Player.class);
			}
			return M_getUser.invoke(getPermissionManager(), player);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}

	public static void setGroup(Player player, String groupName) {
		Object group = getGroups(groupName);
		if(group == null) return;
		setParents(player, group);
	}

	private static void setParents(Player player, Object parents) {
		try {
			Object user = getPermissionUser(player);
			if (user == null) return;
			if (M_setParents == null) {
				M_setParents = user.getClass().getMethod("setParents", List.class);
			}
			M_setParents.invoke(user, parents);
		} catch (ReflectiveOperationException e) {
			return;
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
