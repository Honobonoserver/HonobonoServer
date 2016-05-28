package com.github.syuchan1005.honobonoserver.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

/**
 * Created by syuchan on 2016/02/20.
 */
public class InventoryUtil {

	public static String serialize(Inventory inventory) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
		dataOutput.writeInt(inventory.getSize());
		for (int i = 0; i < inventory.getSize(); i++) 	dataOutput.writeObject(inventory.getItem(i));
		dataOutput.close();
		return Base64Coder.encodeLines(outputStream.toByteArray());
	}

	public static Inventory deserialize(String data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
		BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
		Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
		for (int i = 0; i < inventory.getSize(); i++) inventory.setItem(i, (ItemStack) dataInput.readObject());
		dataInput.close();
		return inventory;
	}
}
