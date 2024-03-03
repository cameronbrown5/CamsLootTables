package me.thecamzone.Rarities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import me.thecamzone.Utils.ItemFactory;
import me.thecamzone.Utils.ItemsFile;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

public class RarityItemHandler {

	private static RarityItemHandler rarityItemHandler;

	private RarityItemHandler() {}

	private HashMap<String, RarityItem> items = new HashMap<>();

	public static RarityItemHandler getInstance() {
		if (rarityItemHandler == null)
			rarityItemHandler = new RarityItemHandler();
		return rarityItemHandler;
	}

	public void load() {
		items.clear();
		
		ConfigurationSection itemsSection = ItemsFile.get().getConfigurationSection("items");

		if(itemsSection == null) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid items.yml");
			return;
		}
		
		for (String itemName : itemsSection.getKeys(false)) {
			String configMaterial = itemsSection.getString(itemName + ".item");
			
			if(configMaterial == null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Could not find material for \"" + itemName + "\" in items.yml.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				continue;
			}
			
			String[] namespacedID = ItemFactory.convertNamespacedID(configMaterial);
			
			if(namespacedID.length == 0) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid item format for \"" + itemName + "\" in items.yml.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				continue;
			}
			
			String namespace = namespacedID[0];
			String materialName = namespacedID[1];
			
			ItemStack item = ItemFactory.getItemStack(namespace, materialName);
			
			if(item == null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Could not find item \"" + configMaterial + "\" for \"" + itemName + "\" in items.yml");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				continue;
			}

//			if(potionMaterials.contains(item.getType())) {
//				String configEffect = itemsSection.getString(itemName + ".potion-effect");
//
//				if(configEffect == null) {
//					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
//					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Potion item was specified for " + itemName + "\" in items.yml, but no potion effect was specified.");
//					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Specify a potion effect by adding a \"potion-effect: <PotionEffectType>\" line under the item type.");
//					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] You can find potion effect IDs here: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html");
//					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
//					continue;
//				}
//
//				PotionEffectType effectType = PotionEffectType.getByName(configEffect);
//
//				if(effectType == null) {
//					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
//					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Could not find potion effect type \"" + configEffect + "\" for " + itemName + "\" in items.yml");
//					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
//					continue;
//				}
//			}

			HashMap<Rarity, List<String>> rarityArgs = new HashMap<>();
			
			ConfigurationSection raritiesSection = itemsSection.getConfigurationSection(itemName + ".rarity");
			
			for(String rarityName : raritiesSection.getKeys(false)) {
				Rarity rarity = RarityHandler.getInstance().getRarity(rarityName);
				
				if(rarity == null) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Could not find rarity \"" + rarityName + "\" for \"" + itemName + "\" in items.yml");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					continue;
				}
				
				List<String> args = raritiesSection.getStringList(rarityName);
				
				rarityArgs.put(rarity, args);
			}
			
			RarityItem rarityItem = new RarityItem(itemName, item, rarityArgs);
			items.put(itemName, rarityItem);
		}
	}
	
	public HashMap<String, RarityItem> getRarityItems() {
		return items;
	}

	public RarityItem getRarityItem(String rarityName) {
		return items.get(rarityName);
	}

}
