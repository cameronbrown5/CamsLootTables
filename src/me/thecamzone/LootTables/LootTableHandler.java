package me.thecamzone.LootTables;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import dev.lone.itemsadder.api.CustomStack;
import me.thecamzone.Utils.DataFile;
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
		ConfigurationSection lootTableSection = DataFile.get().getConfigurationSection("loottables");

		if(lootTableSection == null) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid data.yml");
			return;
		}
		
		for (String lootTableName : lootTableSection.getKeys(false)) {
			LootTable lootTable = new LootTable(lootTableName);
			
			List<String> lootTableItem = lootTableSection.getStringList(lootTableName + ".items");
			for(String lootItemEntry : lootTableItem) {
				String[] lootItemInfo = lootItemEntry.split(" ");
				
				if(lootItemInfo.length < 3) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid item entry for \"" + lootTableName + "\".");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid entry:");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemEntry + "\"");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Correct syntax: \"<Amount> <Item> <Chance>\"");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					return;
				}
				
				Integer lootItemAmount;
				try {
					lootItemAmount = Integer.parseInt(lootItemInfo[0]);
				} catch (NumberFormatException e) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid item entry for \"" + lootTableName + "\".");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemInfo[0] + "\" is not a valid integer.");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid entry:");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemEntry + "\"");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					return;
				}
				
				String[] lootItemStackInfo = lootItemInfo[1].split(":");
				String namespace;
				String materialName;
				
				if(lootItemStackInfo.length == 0) {
					return;
				}
				else if(lootItemStackInfo.length == 2) {
					namespace = lootItemStackInfo[0];
					materialName = lootItemStackInfo[1];
				} else {
					namespace = "minecraft";
					materialName = lootItemStackInfo[0];
				}
				
				ItemStack item = new ItemStack(Material.AIR);
				
				if(namespace.equalsIgnoreCase("minecraft")) {
					Material material = Material.matchMaterial(materialName);
					if(material == null) {
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid item entry for \"" + lootTableName + "\".");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + namespace + ":" + materialName + "\" is not a valid item stack.");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid entry:");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemEntry + "\"");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
						return;
					}
					
					item = new ItemStack(material);
				} else {
					CustomStack stack = CustomStack.getInstance(namespace.toLowerCase() + ":" + materialName.toLowerCase());
					if(stack == null) {
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid item entry for \"" + lootTableName + "\".");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + namespace + ":" + materialName + "\" is not a valid item stack from items adder.");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid entry:");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemEntry + "\"");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
						return;
					}
					
					item = stack.getItemStack();
				}
				
				item.setAmount(lootItemAmount);
				
				Double chance;
				try {
					chance = Double.parseDouble(lootItemInfo[2].replace("%", ""));
				} catch (NumberFormatException e) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid item entry for \"" + lootTableName + "\".");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemInfo[2] + "\" is not a valid double.");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid entry:");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + lootItemEntry + "\"");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					return;
				}
				
				ItemStackChance itemStackChance = new ItemStackChance(item, chance);
				lootTable.addItem(itemStackChance);
			}
			
			lootTables.put(lootTableName, lootTable);
		}
		Bukkit.getConsoleSender().sendMessage("[CamsLootTables] Loaded loot tables.");
	}
	
	public LootTable getLootTable(String lootTableName) {
		if(!lootTables.containsKey(lootTableName)) {
			return null;
		}
		
		return lootTables.get(lootTableName);
	}
	
}
