package co.honobono.honobonoserver.constructor;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LanguageHelper {
	private Map<String, FileConfiguration> Configs = new HashMap<>();

	public LanguageHelper(Plugin plugin) throws IOException, InvalidConfigurationException {
		JarFile jar = null;
		try {
			jar = new JarFile(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
			for (Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements();) {
				String Name = e.nextElement().getName();
				if (!Name.endsWith(".lang")) continue;
				String[] name = Name.split("/");
				Configs.put(name[name.length - 1].replaceAll(".lang", ""), getConfig(plugin, Name));
			}
		} finally {
			jar.close();
		}
	}

	public String getString(CommandSender sender, String path) {
		if(sender instanceof Player) {
			this.getString((Player)sender, path);
		}
		return path;
	}

	public String getString(Player player, String path) {
		try {
			return this.getString(getLocale(player), path);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return path;
		}
	}

	public String getString(String LangName, String path) {
		if(Configs.containsKey(LangName)) {
			return Configs.get(LangName).getString(path);
		}
		return path;
	}

	public List<String> getStringList(CommandSender sender, String path) {
		if(sender instanceof Player) {
			this.getStringList((Player)sender, path);
		}
		return Arrays.asList(path);
	}

	public List<String> getStringList(Player player, String path) {
		try {
			return this.getStringList(getLocale(player), path);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return Arrays.asList(path);
		}
	}

	public List<String> getStringList(String LangName, String path) {
		if(Configs.containsKey(LangName)) {
			return Configs.get(LangName).getStringList(path);
		}
		return Arrays.asList(path);
	}

	public FileConfiguration getConfig(Plugin plugin, String filename) throws IOException, InvalidConfigurationException {
		File file = new File(plugin.getDataFolder(), filename);
		if (!file.exists()) plugin.saveResource(filename, false);
		Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
		FileConfiguration conf = new YamlConfiguration();
		conf.load(reader);
		return conf;
	}

	public static String getLocale(Player player) throws ReflectiveOperationException {
		Method method = player.getClass().getDeclaredMethod("getHandle");
		Object o = method.invoke(player);
		Field field = o.getClass().getField("locale");
		return (String)field.get(o);
	}
}
