package com.github.syuchan1005.honobonoserver.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by syuchan on 2016/05/08.
 */
public class AnvilGUI {
	private Player player;
	private AnvilClickEventHandler handler;
	private HashMap<AnvilSlot, ItemStack> items = new HashMap<AnvilSlot, ItemStack>();
	private Inventory inv;
	private Listener listener;

	public AnvilGUI(JavaPlugin plugin, Player player, final AnvilClickEventHandler handler) {
		this.player = player;
		this.handler = handler;

		this.listener = new Listener(){
			@EventHandler
			public void onInventoryClick(InventoryClickEvent event){
				if(event.getWhoClicked() instanceof Player){
					Player clicker = (Player) event.getWhoClicked();

					if(event.getInventory().equals(inv)){
						event.setCancelled(true);

						ItemStack item = event.getCurrentItem();
						int slot = event.getRawSlot();
						String name = "";

						if(item != null){
							if(item.hasItemMeta()){
								ItemMeta meta = item.getItemMeta();

								if(meta.hasDisplayName()){
									name = meta.getDisplayName();
								}
							}
						}

						AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name);

						handler.onAnvilClick(clickEvent);

						if(clickEvent.getWillClose()){
							event.getWhoClicked().closeInventory();
						}

						if(clickEvent.getWillDestroy()){
							destroy();
						}
					}
				}
			}

			@EventHandler
			public void onInventoryClose(InventoryCloseEvent event){
				if(event.getPlayer() instanceof Player){
					Player player = (Player) event.getPlayer();
					Inventory inv = event.getInventory();

					if(inv.equals(AnvilGUI.this.inv)) {
						inv.clear();
						destroy();
					}
				}
			}

			@EventHandler
			public void onPlayerQuit(PlayerQuitEvent event){
				if(event.getPlayer().equals(getPlayer())){
					destroy();
				}
			}
		};

		Bukkit.getPluginManager().registerEvents(listener, plugin); //Replace with instance of main class
	}

	public enum AnvilSlot {
		INPUT_LEFT(0),
		INPUT_RIGHT(1),
		OUTPUT(2);

		private int slot;

		AnvilSlot(int slot){
			this.slot = slot;
		}

		public int getSlot(){
			return slot;
		}

		public static AnvilSlot bySlot(int slot){
			for(AnvilSlot anvilSlot : values()){
				if(anvilSlot.getSlot() == slot){
					return anvilSlot;
				}
			}
			return null;
		}
	}

	public interface AnvilClickEventHandler {
		void onAnvilClick(AnvilClickEvent event);
	}

	public class AnvilClickEvent {
		private AnvilSlot slot;

		private String name;

		private boolean close = true;
		private boolean destroy = true;

		public AnvilClickEvent(AnvilSlot slot, String name){
			this.slot = slot;
			this.name = name;
		}

		public AnvilSlot getSlot(){
			return slot;
		}

		public String getName(){
			return name;
		}

		public boolean getWillClose(){
			return close;
		}

		public void setWillClose(boolean close){
			this.close = close;
		}

		public boolean getWillDestroy(){
			return destroy;
		}

		public void setWillDestroy(boolean destroy){
			this.destroy = destroy;
		}
	}

	public Player getPlayer(){
		return player;
	}

	public void setSlot(AnvilSlot slot, ItemStack item){
		items.put(slot, item);
	}

	public void open() throws ReflectiveOperationException {
		Object p = Reflection.EntityPlayer.get(player);
		Object container = Reflection.ContainerAnvil.get(p);
		inv = Reflection.ContainerAnvil.Inventory(container);
		for(AnvilSlot slot : items.keySet()) inv.setItem(slot.getSlot(), items.get(slot));
		int c = Reflection.EntityPlayer.nextContainerCounter(p);
		Reflection.EntityPlayer.sendPacket(p, Reflection.PacketPlayOutOpenWindow.create(c, "minecraft:anvil"));
		Reflection.EntityPlayer.ActiveContainer.set(p, container);
		Reflection.EntityPlayer.ActiveContainer.setWindowId(p, c);
		Reflection.EntityPlayer.ActiveContainer.setAddSlotListener(p);
	}

	public void destroy(){
		player = null;
		handler = null;
		items = null;
		HandlerList.unregisterAll(listener);
		listener = null;
	}

	public static class Reflection {
		private static Constructor containerAnvilConstructor, containerBlockPositionConstructor;
		private static Field checkReachableField;
		private static Method getBukkitViewMethod, getTopInventoryMethod;
		private static Constructor PacketPlayOutOpenWindowConstructor, ChatMessageConstructor;

		static class ContainerAnvil {
			public static Object get(Object entityHuman) throws ReflectiveOperationException {
				if(containerAnvilConstructor == null) {
					containerAnvilConstructor = Class
							.forName("net.minecraft.server." + getVersion() + ".ContainerAnvil").getConstructors()[0];
				}
				Object anvil = containerAnvilConstructor.newInstance(EntityPlayer.getPlayerInventory(entityHuman)
						, EntityPlayer.getWorld(entityHuman), createBlockPosition(0, 0, 0), entityHuman);
				if(checkReachableField == null) checkReachableField = anvil.getClass().getField("checkReachable");
				checkReachableField.set(anvil, false);
				return anvil;
			}

			public static Inventory Inventory(Object containerAnvil) throws ReflectiveOperationException {
				if(getBukkitViewMethod == null) getBukkitViewMethod = containerAnvil.getClass().getMethod("getBukkitView");
				Object view = getBukkitViewMethod.invoke(containerAnvil);
				if(getTopInventoryMethod == null) getTopInventoryMethod = view.getClass().getMethod("getTopInventory");
				return ((Inventory) getTopInventoryMethod.invoke(view));
			}
		}

		static class EntityPlayer {
			private static Class PacketClass;
			private static Field inventoryField, worldField, playerConnectionField;
			private static Method nextContainerCounterMethod, sendPacketMethod, getHandleMethod;

			public static Object get(Player player) throws ReflectiveOperationException {
				if(getHandleMethod == null) getHandleMethod = player.getClass().getMethod("getHandle");
				return getHandleMethod.invoke(player);
			}

			public static Object getPlayerInventory(Object entityPlayer) throws ReflectiveOperationException {
				if(inventoryField == null) inventoryField = entityPlayer.getClass().getField("inventory");
				return inventoryField.get(entityPlayer);
			}

			public static Object getWorld(Object entityPlayer) throws ReflectiveOperationException {
				if(worldField == null) worldField = entityPlayer.getClass().getField("world");
				return worldField.get(entityPlayer);
			}

			public static int nextContainerCounter(Object entityPlayer) throws ReflectiveOperationException {
				if(nextContainerCounterMethod == null) nextContainerCounterMethod = entityPlayer.getClass().getMethod("nextContainerCounter");
				return ((int) nextContainerCounterMethod.invoke(entityPlayer));
			}

			public static void sendPacket(Object entityPlayer, Object packet) throws ReflectiveOperationException {
				if(PacketClass == null) PacketClass = Class.forName("net.minecraft.server." + getVersion() + ".Packet");
				if(playerConnectionField == null) playerConnectionField = entityPlayer.getClass().getField("playerConnection");
				Object con = playerConnectionField.get(entityPlayer);
				if(sendPacketMethod == null) sendPacketMethod = con.getClass().getMethod("sendPacket", PacketClass);
				sendPacketMethod.invoke(con, packet);
			}

			static class ActiveContainer {
				private static Class ICraftingClass;
				private static Method addSlotListenerMethod;
				private static Field selfField, windowIdField;

				private static Object get(Object entityPlayer) throws ReflectiveOperationException {
					if(selfField == null) selfField = entityPlayer.getClass().getField("activeContainer");
					return selfField.get(entityPlayer);
				}

				public static void set(Object entityPlayer, Object container) throws ReflectiveOperationException {
					if(selfField == null) selfField = entityPlayer.getClass().getField("activeContainer");
					selfField.set(entityPlayer, container);
				}

				public static void setWindowId(Object entityPlayer, int id) throws ReflectiveOperationException {
					Object actiCon = get(entityPlayer);
					if(windowIdField == null) windowIdField = actiCon.getClass().getField("windowId");
					windowIdField.set(actiCon, id);
				}

				public static void setAddSlotListener(Object entityPlayer) throws ReflectiveOperationException {
					if(ICraftingClass == null) ICraftingClass = Class.forName("net.minecraft.server." + getVersion() + ".ICrafting");
					Object actiCon = get(entityPlayer);
					if(addSlotListenerMethod == null) addSlotListenerMethod = actiCon.getClass().getMethod("addSlotListener", ICraftingClass);
					addSlotListenerMethod.invoke(actiCon, entityPlayer);
				}
			}

		}

		public static Object createBlockPosition(double x, double y, double z) throws ReflectiveOperationException {
			if(containerBlockPositionConstructor == null) {
				containerBlockPositionConstructor = Class.forName("net.minecraft.server." + getVersion() + ".BlockPosition")
						.getConstructor(double.class, double.class, double.class);
			}
			return containerBlockPositionConstructor.newInstance(x, y, z);
		}

		public static Object createChatMessage(String name) throws ReflectiveOperationException {
			if(ChatMessageConstructor == null) ChatMessageConstructor = Class.forName("net.minecraft.server." + getVersion() + ".ChatMessage").getConstructors()[0];
			return ChatMessageConstructor.newInstance(name, new Object[]{});
		}

		static class PacketPlayOutOpenWindow {
			public static Object create(int i, String invName) throws ReflectiveOperationException {
				Object chatMessage = createChatMessage("");
				if (PacketPlayOutOpenWindowConstructor == null) {
					PacketPlayOutOpenWindowConstructor = Class.forName("net.minecraft.server." + getVersion() + ".PacketPlayOutOpenWindow")
							.getConstructor(int.class, String.class, chatMessage.getClass().getSuperclass().getInterfaces()[0], int.class);
				}
				return PacketPlayOutOpenWindowConstructor.newInstance(i, invName, chatMessage, 0);
			}
		}

		private static String getVersion() {
			final String packageName = Bukkit.getServer().getClass().getPackage().getName();
			return packageName.substring(packageName.lastIndexOf('.') + 1);
		}

	}
}