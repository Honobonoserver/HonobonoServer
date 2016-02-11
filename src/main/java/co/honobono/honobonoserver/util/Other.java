package co.honobono.honobonoserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Other {

	public static String color(String text, Player player) {
		if (player != null) text = text.replaceAll("<player>", player.getName());
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static FileConfiguration getConfig(Plugin plugin, String filename) throws IOException, InvalidConfigurationException {
		File file = new File(plugin.getDataFolder(), filename);
		if (!file.exists()) plugin.saveResource(filename, false);
		Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
		FileConfiguration conf = new YamlConfiguration();
		conf.load(reader);
		return conf;
	}
}
