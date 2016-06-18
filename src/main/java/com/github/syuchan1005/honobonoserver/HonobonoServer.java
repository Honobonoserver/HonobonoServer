package com.github.syuchan1005.honobonoserver;

import com.github.syuchan1005.honobonoserver.command.PocketChestCommand;
import com.github.syuchan1005.honobonoserver.constructor.LanguageHelper;
import com.github.syuchan1005.honobonoserver.constructor.RegistManager;
import com.github.syuchan1005.honobonoserver.constructor.SQLite;
import com.github.syuchan1005.honobonoserver.runnable.EnderDragonMoveRunnable;
import com.github.syuchan1005.honobonoserver.runnable.WitherMoveRunnable;
import com.github.syuchan1005.honobonoserver.util.InventoryUtil;
import com.github.syuchan1005.honobonoserver.util.Other;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HonobonoServer extends JavaPlugin {
	private RegistManager manager;
	private FileConfiguration config;
	private FileConfiguration inv;
	private LanguageHelper langs;
	private SQLite SQL;

	@Override
	public void onEnable() {
		try {
			this.config = Other.getConfig(this, "config.yml");
			this.inv = Other.getConfig(this, "pocketchest.yml");
			this.langs = new LanguageHelper(this);
			this.SQL = new SQLite(new File(this.getDataFolder(), "Setting.db"));
			this.SQL.add("PlayerUUID", SQLite.Casts.TEXT, SQLite.Casts.NOTNULL, SQLite.Casts.UNIQUE)
					.add("PlayerName", SQLite.Casts.TEXT, SQLite.Casts.NOTNULL)
					.add("Time", SQLite.Casts.TIMESTAMP)
					.add("Home-World", SQLite.Casts.INTEGER)
					.add("Home-X", SQLite.Casts.INTEGER)
					.add("Home-Y", SQLite.Casts.INTEGER)
					.add("Home-Z", SQLite.Casts.INTEGER).create("Home");
			this.manager = new RegistManager(this);
			File addon = new File(this.getDataFolder(), "addon");
			addon.mkdir();
			for(File file : addon.listFiles((dir, name) -> name.endsWith(".jar"))) {
				this.manager.loadClasses(file, false);
				this.getLogger().info("LoadComplete: " + file.getName());
			}
			this.manager.registerCommand("hn", "HonobonoServer's Plugin Command.", "/hn(h) <options>", "hns.c", "You don't have Permission.", "h");
		} catch (ReflectiveOperationException | IOException | SQLException | InvalidConfigurationException e) {
			e.printStackTrace();

		}
		new EnderDragonMoveRunnable(this).runTaskTimer(this, 0, 5);
		new WitherMoveRunnable().runTaskTimer(this, 0, 5);
	}

	@Override
	public void onDisable() {
		Map<String, Inventory> map = PocketChestCommand.getMap();
		if (map != null) {
			Map<String, String> a = new HashMap<>();
			try {
				for (Map.Entry<String, Inventory> entry : map.entrySet()) {
					a.put(entry.getKey(), InventoryUtil.serialize(entry.getValue()));
				}
				this.getInv().set("Inventories", Arrays.asList(a));
				this.getInv().save(new File(this.getDataFolder(), "pocketchest.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public FileConfiguration getConfigFile() {
		return this.config;
	}

	public void reloadConfigFile() throws IOException, InvalidConfigurationException {
		this.config = Other.getConfig(manager.getPlugin(), "config.yml");
	}

	public SQLite getSQLite() {
		return this.SQL;
	}

	public RegistManager getManager() {
		return this.manager;
	}

	public LanguageHelper getLanguages() {
		return this.langs;
	}

	public FileConfiguration getInv() {
		return this.inv;
	}

}
