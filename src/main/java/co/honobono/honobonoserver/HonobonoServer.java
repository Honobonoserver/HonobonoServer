package co.honobono.honobonoserver;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import co.honobono.honobonoserver.command.PocketChestCommand;
import co.honobono.honobonoserver.constructor.LanguageHelper;
import co.honobono.honobonoserver.util.InventoryUtil;
import com.sun.javafx.collections.MappingChange;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import co.honobono.honobonoserver.constructor.RegistManager;
import co.honobono.honobonoserver.constructor.SQLite;
import co.honobono.honobonoserver.constructor.SQLite.Casts;
import co.honobono.honobonoserver.runnable.EnderDragonMoveRunnable;
import co.honobono.honobonoserver.runnable.WitherMoveRunnable;
import co.honobono.honobonoserver.util.Other;

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
			this.SQL.add("PlayerUUID", Casts.TEXT, Casts.NOTNULL, Casts.UNIQUE)
					.add("PlayerName", Casts.TEXT, Casts.NOTNULL)
					.add("Time", Casts.TIMESTAMP)
					.add("Home-World", Casts.INTEGER)
					.add("Home-X", Casts.INTEGER)
					.add("Home-Y", Casts.INTEGER)
					.add("Home-Z", Casts.INTEGER).create("Home");
			this.manager = new RegistManager(this);
			this.manager.registerCommand("hn", "HonobonoServer's Plugin Command.", "/hn(h) <options>", "honobonoserver.command", "You don't have Permission.", "h");
		} catch (ReflectiveOperationException | IOException | SQLException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		new EnderDragonMoveRunnable().runTaskTimer(this, 0, 5);
		new WitherMoveRunnable().runTaskTimer(this, 0, 5);
	}

	@Override
	public void onDisable() {
		Map<String, Inventory> map = PocketChestCommand.getMap();
		if(map != null) {
			Map<String, String> a = new HashMap<>();
			try {
				for(Map.Entry<String, Inventory> entry : map.entrySet()) {
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
