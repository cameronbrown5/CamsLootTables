package me.thecamzone.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIUtil{
	
	public static void setFrame(Inventory inventory, Material frameMaterial) {
		ItemStack filler = getFiller(frameMaterial);
		int inventorySize = inventory.getSize();
		for (int slot = 0; slot < inventory.getSize(); slot++) {
			if ((slot <= 9 || slot >= 17) && (slot <= 18 || slot >= 26 || inventorySize <= 27)
					&& (slot <= 27 || slot >= 35 || inventorySize <= 36)
					&& (slot <= 36 || slot >= 44 || inventorySize <= 45))
				inventory.setItem(slot, filler);
		}
	}

	public static void fillEmpty(Inventory inventory, Material fillMaterial) {
		ItemStack filler = getFiller(fillMaterial);
		while (inventory.firstEmpty() != -1)
			inventory.setItem(inventory.firstEmpty(), filler);
	}

	public static void setPageItems(Inventory inventory, int page, boolean hasNextPage) {
		if (page > 1)
			inventory.setItem(inventory.getSize() - 8, getGUIItem(Material.PAPER, "&6Previous Page", null));
		if (hasNextPage)
			inventory.setItem(53, getGUIItem(Material.PAPER, "&6Next Page", null));
	}

	public static void addBackItem(Inventory inventory) {
		inventory.setItem(0, getGUIItem(Material.RED_STAINED_GLASS_PANE, "&c&lGo Back", null));
	}

	public static ItemStack getGUIItem(Material material, String name, List<String> lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		if (name != null)
			meta.setDisplayName(StringUtil.formatColor(name));
		if (lore != null) {
			ListIterator<String> loreIterator = lore.listIterator();
			while (loreIterator.hasNext())
				loreIterator.set(StringUtil.formatColor(loreIterator.next()));
			meta.setLore(lore);
		}
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        List<String> formattedLore = new ArrayList<>();
        for(String s : lore) {
        	formattedLore.add(StringUtil.formatColor(s));
        }
        
        // Set the name of the item
        meta.setDisplayName(StringUtil.formatColor(name));

        // Set the lore of the item
        meta.setLore(formattedLore);

        item.setItemMeta(meta);
        
        return item;
    }

	public static boolean isFiller(ItemStack item) {
		if (item == null)
			return false;
		if (!item.hasItemMeta())
			return false;
		return item.getItemMeta().getDisplayName().equals(" ");
	}

	public static int getFramedInventorySize(int itemAmount) {
		int inventorySize = 18 + 9 * (int) Math.ceil(itemAmount / 7.0D);
		if (inventorySize > 54)
			return 54;
		return Math.max(27, inventorySize);
	}

	public static int getInitialIndex(int page) {
		return (page == 1) ? 0 : ((page - 1) * 28);
	}

	public static ItemStack getFiller(Material material) {
		ItemStack filler = new ItemStack(material);
		ItemMeta meta = filler.getItemMeta();
		meta.setDisplayName(" ");
		filler.setItemMeta(meta);
		return filler;
	}
}
