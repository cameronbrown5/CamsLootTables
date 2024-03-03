package me.thecamzone.Rarities;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import me.thecamzone.CamsLootTables;
import me.thecamzone.Utils.StringUtil;
import net.md_5.bungee.api.ChatColor;

public class RarityHandler {

	private static RarityHandler rarityHandler;
	
	private RarityHandler() {}
	
	private HashMap<String, Rarity> rarities = new HashMap<>();
	
	public static RarityHandler getInstance() {
		if(rarityHandler == null)
			rarityHandler = new RarityHandler();
		return rarityHandler;
	}
	
	public void load() {
		rarities.clear();
		
		ConfigurationSection raritySection = CamsLootTables.getInstance().getConfig().getConfigurationSection("rarities");

//		if(raritySection == null) {
//			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid \"rarities\" section in config.yml");
//			return;
//		}
		
		Integer totalChance = 0;
		
		HashMap<String, Rarity> tempRarities = new HashMap<>();
		
		for (String rarityName : raritySection.getKeys(false)) {
			
			String displayName = raritySection.getString(rarityName + ".display-name");
			if(displayName == null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Error in file \"config.yml\".");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid display name for \"" + rarityName + "\".");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				continue;
			}
			
			displayName = StringUtil.formatColor(displayName);
			
			Integer chance = raritySection.getInt(rarityName + ".chance");
			if(chance == 0) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Error in file \"config.yml\".");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid chance for \"" + rarityName + "\".");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Chance must be between 1 and 100.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				continue;
			}
			
			totalChance += chance;
			
			Rarity rarity = new Rarity(rarityName, displayName, chance);
			tempRarities.put(rarityName, rarity);
		}
		
		if(totalChance != 100) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Error in file \"config.yml\".");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Rarity chances do not add up to 100%.");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
			return;
		}

		tempRarities.put("all", new Rarity(null, null, null));
		
		rarities = tempRarities;
		Bukkit.getConsoleSender().sendMessage("[CamsLootTables] Loaded rarities.");
	}

	public HashMap<String, Rarity> getRarities() {
		return rarities;
	}
	
	public Rarity getRarity(String rarityName) {
		return rarities.get(rarityName);
	}
	
}
