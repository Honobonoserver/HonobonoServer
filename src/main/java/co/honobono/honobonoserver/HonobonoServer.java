package co.honobono.honobonoserver;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import co.honobono.honobonoserver.constructor.LanguageHelper;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import co.honobono.honobonoserver.constructor.RegistManager;
import co.honobono.honobonoserver.constructor.SQLite;
import co.honobono.honobonoserver.constructor.SQLite.Casts;
import co.honobono.honobonoserver.runnable.EnderDragonMoveRunnable;
import co.honobono.honobonoserver.runnable.WitherMoveRunnable;
import co.honobono.honobonoserver.util.Other;

public class HonobonoServer extends JavaPlugin {
	private static Plugin plugin;
	private static RegistManager manager;
	private static FileConfiguration config;
	private static LanguageHelper langs;
	private static SQLite SQL;
	private static LinkedHashMap<String, Casts[]> map = new LinkedHashMap<>();{
		map.put("PlayerUUID", Casts.toArray(Casts.TEXT, Casts.NOTNULL, Casts.UNIQUE));
		map.put("PlayerName", Casts.toArray(Casts.TEXT, Casts.NOTNULL));
		map.put("Time", Casts.toArray(Casts.TIMESTAMP));
		map.put("Home-World", Casts.toArray(Casts.TEXT));
		map.put("Home-X", Casts.toArray(Casts.INTEGER));
		map.put("Home-Y", Casts.toArray(Casts.INTEGER));
		map.put("Home-Z", Casts.toArray(Casts.INTEGER));
	}

	@Override
	public void onEnable() {
		try {
			plugin = this;
			config = Other.getConfig(this, "config.yml");
			langs = new LanguageHelper(this);
			getSQLite();
			SQL.create("Home", map);
			manager = new RegistManager(this);
			manager.registerCommand("hn", "HonobonoServer's Plugin Command.", "/hn(h) <options>", "honobonoserver.command", "You don't have Permission.", "h");
		} catch (ReflectiveOperationException | IOException | SQLException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		new EnderDragonMoveRunnable().runTaskTimer(this, 0, 5);
		new WitherMoveRunnable().runTaskTimer(this, 0, 5);
	}

	public static FileConfiguration getConfigFile() {
		return config;
	}

	public static SQLite getSQLite() {
		if (SQL == null) {
			try {
				SQL = new SQLite(new File(plugin.getDataFolder(), "Setting.db"));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return SQL;
	}

	public static RegistManager getManager() {
		return manager;
	}

	public static LanguageHelper getLanguages() {
		return langs;
	}

}
