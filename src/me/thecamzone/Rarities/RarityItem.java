package me.thecamzone.Rarities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.thecamzone.CamsLootTables;

public class RarityItem extends ItemStack {

	private String name;
	private ItemStack item;
	private HashMap<Rarity, List<String>> rarities = new HashMap<>();
	private Rarity rarity;

	public RarityItem(String name, ItemStack item, HashMap<Rarity, List<String>> rarities) {
		this.name = name;
		this.item = item;
		this.rarities = rarities;
		
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.getPersistentDataContainer().set(CamsLootTables.getInstance().getRarityItemKey(), PersistentDataType.STRING, name);
		
		item.setItemMeta(itemMeta);
	}

	public String getName() {
		return name;
	}

	public ItemStack getItem() {
		return item;
	}

	public HashMap<Rarity, List<String>> getItemRarities() {
		return rarities;
	}

	public void setRarity(Rarity rarity) {
		this.rarity = rarity;
	}

	public List<String> getRarityArgs(Rarity rarity) {
		return rarities.getOrDefault(rarity, new ArrayList<String>());
	}

	public Rarity rollRarity() {
		int totalChances = 0;

		Set<Rarity> availableRarities = rarities.keySet();

		if(availableRarities.size() == 0) {
			Bukkit.getConsoleSender().sendMessage("No available rarities");
			return null;
		}
		
		List<Rarity> listAvailableRarities = new ArrayList<>(availableRarities);

		Collections.sort(listAvailableRarities, new Comparator<Rarity>() {
			@Override
			public int compare(Rarity r1, Rarity r2) {
				return r1.getChance().compareTo(r2.getChance());
			}
		});

		Collections.reverse(listAvailableRarities);
		
		for (Rarity rarity : availableRarities) {
			totalChances += rarity.getChance();
		}

		Random random = new Random();
		Integer randomInt = random.nextInt(totalChances);

		Integer chance = 0;
		for(int i = 0; i < listAvailableRarities.size(); i++) {
			Rarity rarity = listAvailableRarities.get(i);
			
//			Bukkit.getConsoleSender().sendMessage("==================");
//			Bukkit.getConsoleSender().sendMessage("randomInt = " + randomInt);
//			Bukkit.getConsoleSender().sendMessage("chance = " + chance);
			
			if(listAvailableRarities.size() == 1 || i == (listAvailableRarities.size() - 1)) {
//				Bukkit.getConsoleSender().sendMessage("Returning only option");
				return rarity;
			}
			
			if (randomInt <= chance) {
				try {
					listAvailableRarities.get(i - 1);
				} catch (IndexOutOfBoundsException e) {
//					Bukkit.getConsoleSender().sendMessage("randomInt is less than chance. Returning " + rarity.getDisplayName());
					return rarity;
				}
				
//				Bukkit.getConsoleSender().sendMessage("randomInt is less than chance. Returning " + listAvailableRarities.get(i - 1).getDisplayName());
				return listAvailableRarities.get(i - 1);
				
			} else {
				chance = chance + rarity.getChance();
//				Bukkit.getConsoleSender().sendMessage("randomInt is greater than chance. Adding " + rarity.getChance());
				continue;
			}
		}
		
		return null;
	}

	public Rarity getRarity() {
		return rarity;
	}

}
