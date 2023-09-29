package me.thecamzone.LootTables;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import me.thecamzone.Utils.ItemFactory;
import me.thecamzone.Utils.LootTablesFile;
import net.md_5.bungee.api.ChatColor;

public class LootTableHandler {

	private static LootTableHandler lootTableHandler;
	
	private HashMap<String, LootTable> lootTables = new HashMap<String, LootTable>();
	
	private LootTableHandler() {}
	
	public static LootTableHandler getInstance() {
		if(lootTableHandler == null)
			lootTableHandler = new LootTableHandler();
		return lootTableHandler;
	}
	
	public void load() {
		lootTables.clear();
		
		ConfigurationSection lootTableSection = LootTablesFile.get().getConfigurationSection("loottables");

		if(lootTableSection == null) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid loottables.yml");
			return;
		}
		
		for (String lootTableName : lootTableSection.getKeys(false)) {
			LootTable lootTable = new LootTable(lootTableName);
			
			List<String> lootTableItem = lootTableSection.getStringList(lootTableName + ".items");
			for(String lootItemEntry : lootTableItem) {
				String[] lootItemInfo = lootItemEntry.split(" ");
				
				boolean failed = false;
				
				if(lootItemInfo.length < 3) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid item entry for \"" + lootTableName + "\".");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid entry:");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemEntry + "\"");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Correct syntax: \"<Quantity> <Item> <Chance> <QuantityChance> [Spread (true/false)]\"");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					failed = true;
				}
				
				Integer lootItemAmount = 0;
				try {
					lootItemAmount = Integer.parseInt(lootItemInfo[0]);
				} catch (NumberFormatException e) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid item entry for \"" + lootTableName + "\".");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemInfo[0] + "\" is not a valid integer.");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid entry:");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemEntry + "\"");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					failed = true;
				}
				
				String[] namespacedID = ItemFactory.convertNamespacedID(lootItemInfo[1]);
				if(namespacedID.length == 0) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid item entry for \"" + lootTableName + "\".");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid entry:");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemInfo[1] + "\"");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					return;
				}
			
				String namespace = namespacedID[0];
				String materialName = namespacedID[1];
				
				ItemStack item = ItemFactory.getItemStack(namespace, materialName);
				if(item == null) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid item entry for \"" + lootTableName + "\".");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + namespace + ":" + materialName + "\" is not a valid item.");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				}
				
				Double chance = 0D;
				try {
					chance = Double.parseDouble(lootItemInfo[2].replace("%", ""));
				} catch (NumberFormatException e) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid item entry for \"" + lootTableName + "\".");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemInfo[2] + "\" is not a valid double.");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid entry:");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemEntry + "\"");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					failed = true;
				}
				
				Double quantityChance = 100D;
				if(lootItemAmount > 1) {
					try {
						quantityChance = Double.parseDouble(lootItemInfo[3].replace("%", ""));
					} catch (NumberFormatException e) {
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid item entry for \"" + lootTableName + "\".");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemInfo[3] + "\" is not a valid double.");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] If the item quantity is greater than 1, you must specify a quantity chance.");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Correct syntax: \"<Quantity> <Item> <Chance> <QuantityChance> [Spread (true/false)]\"");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid entry:");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemEntry + "\"");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
						failed = true;
					}
				}
				
				if(failed) {
					continue;
				}
				
				Boolean spread = false;
				if(lootItemInfo.length == 5) {
					spread = Boolean.parseBoolean(lootItemInfo[4]);
				}
				
				ItemStackChance itemStackChance = new ItemStackChance(item, lootItemAmount, chance, quantityChance, spread);
				lootTable.addItem(itemStackChance);
			}
			
			lootTables.put(lootTableName, lootTable);
		}
		Bukkit.getConsoleSender().sendMessage("[CamsLootTables] Loaded loot tables.");
	}
	
	public void loadInheritance() {
		ConfigurationSection lootTableSection = LootTablesFile.get().getConfigurationSection("loottables");

		if(lootTableSection == null) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid loottables.yml");
			return;
		}
		
		for (String lootTableName : lootTableSection.getKeys(false)) {
			List<String> lootTableInheritances = lootTableSection.getStringList(lootTableName + ".inherits");
			
			for(String inheritance : lootTableInheritances) {
				LootTable inheritedLootTable = lootTables.get(inheritance);
				
				if(!lootTables.get(lootTableName).getInherits().contains(inheritedLootTable)) {
					lootTables.get(lootTableName).addInheritedLootTable(inheritedLootTable);
				}
			}
		}
		
		Bukkit.getConsoleSender().sendMessage("[CamsLootTables] Loaded inheritance.");
	}
	
	public LootTable getLootTable(String lootTableName) {
		if(!lootTables.containsKey(lootTableName)) {
			return null;
		}
		
		return lootTables.get(lootTableName);
	}
	
}
