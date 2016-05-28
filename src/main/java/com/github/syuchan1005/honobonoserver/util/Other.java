package com.github.syuchan1005.honobonoserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class Other {

	public static String color(String text, Player player) {
		if (player != null) text = text.replaceAll("<player>", player.getName());
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static Reader getFileReader(Plugin plugin, String filename) throws IOException {
		File file = new File(plugin.getDataFolder(), filename);
		if (!file.exists()) plugin.saveResource(filename, false);
		return new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
	}

	public static FileConfiguration getConfig(Plugin plugin, String filename) throws IOException, InvalidConfigurationException {
		Reader reader = getFileReader(plugin, filename);
		FileConfiguration conf = new YamlConfiguration();
		conf.load(reader);
		return conf;
	}

	public static Location toLocation(String s) {
		String[] loc = s.split(",");
		World w = Bukkit.getWorld(loc[0].substring(31, loc[0].length() - 1));
		double x = Double.valueOf(loc[1].substring(2, loc[1].length()));
		double y = Double.valueOf(loc[2].substring(2, loc[2].length()));
		double z = Double.valueOf(loc[3].substring(2, loc[3].length()));
		float pitch = Float.valueOf(loc[4].substring(6, loc[4].length()));
		float yaw = Float.valueOf(loc[5].substring(4, loc[5].length() - 1));
		Location loc1 = new Location(w, x, y, z, yaw, pitch);
		return loc1;
	}

	public static boolean include(Object obj, Object... objs) {
		return Arrays.stream(objs).anyMatch(o -> o.equals(obj));
	}

	public static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
