package co.honobono.honobonoserver;

import co.honobono.honobonoserver.command.PocketChestCommand;
import co.honobono.honobonoserver.constructor.CustomRecipe;
import co.honobono.honobonoserver.constructor.LanguageHelper;
import co.honobono.honobonoserver.constructor.RegistManager;
import co.honobono.honobonoserver.constructor.SQLite;
import co.honobono.honobonoserver.constructor.SQLite.Casts;
import co.honobono.honobonoserver.runnable.EnderDragonMoveRunnable;
import co.honobono.honobonoserver.runnable.WitherMoveRunnable;
import co.honobono.honobonoserver.util.InventoryUtil;
import co.honobono.honobonoserver.util.Other;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
			CustomRecipe.init(this);
			this.setRecipe();
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
			File addon = new File(this.getDataFolder(), "addon");
			addon.mkdir();
			for(File file : addon.listFiles((dir, name) -> name.endsWith(".jar"))) {
				this.manager.loadClasses(file, false);
				this.getLogger().info("LoadComplete: " + file.getName());
			}
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

	private static void setRecipe() {
		ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 2, false);
		itemMeta.setDisplayName("つよいけん(こなみ)");
		itemStack.setItemMeta(itemMeta);
		CustomRecipe.addRecipe(new CustomRecipe.Shaped(itemStack, new String[]{"DDA", "ADD", "ASA"}
				, new CustomRecipe.ShapedMaterial(new ItemStack(Material.DIAMOND), 'D')
				, new CustomRecipe.ShapedMaterial(new ItemStack(Material.STICK), 'S')));
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
