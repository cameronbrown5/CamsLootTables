package me.thecamzone.Utils;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.thecamzone.CamsLootTables;
import net.md_5.bungee.api.ChatColor;

public class ItemFactory {

	public static ItemStack getDebugItem() {
		ItemStack item = new ItemStack(Material.STICK);
		ItemMeta itemMeta = item.getItemMeta();
		
		itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "LootTable Debug Stick");
		itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right click a sign to spawn" , ChatColor.GRAY + "a loot table chest."));
		itemMeta.getPersistentDataContainer().set(new NamespacedKey(CamsLootTables.getInstance(), "camsloottables"), PersistentDataType.STRING, "debug_stick");
		
		item.setItemMeta(itemMeta);
		return item;
	}
	
}
