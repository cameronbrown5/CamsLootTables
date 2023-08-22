package me.thecamzone.LootTables;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.thecamzone.CamsLootTables;
import me.thecamzone.Rarities.Rarity;
import me.thecamzone.Rarities.RarityItem;
import me.thecamzone.Rarities.RarityItemHandler;
import me.thecamzone.Utils.ItemFactory;

public class ItemStackChance {

	private ItemStack item;
	private Integer amount;
	private Double chance;
	private Double quantityChance;
	private boolean spread; 
	
	public ItemStackChance(ItemStack item, Integer amount, Double chance, Double quantityChance, boolean spread) {
		this.item = item;
		this.amount = amount;
		this.chance = chance;
		this.quantityChance = quantityChance;
		this.spread = spread;
	}

	public ItemStack getItem() {
		if(item == null) {
			return new ItemStack(Material.AIR);
		}
		
		if(item.hasItemMeta()) {
			ItemMeta itemMeta = item.getItemMeta();
			PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
			NamespacedKey namespacedKey = CamsLootTables.getInstance().getRarityItemKey();
			
			if(pdc.has(namespacedKey, PersistentDataType.STRING)) {
				RarityItem rarityItem = RarityItemHandler.getInstance().getRarityItem(pdc.get(namespacedKey, PersistentDataType.STRING));
				
				Rarity rarity = rarityItem.rollRarity();
				rarityItem.setRarity(rarity);
				
				ItemStack enchantedItem = ItemFactory.parseArgs(rarityItem);
				
				ItemStack formattedItem = ItemFactory.formatRarityItem(enchantedItem, rarity);
				
				return formattedItem;
			}
		}
		
		return item;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}

	public Double getChance() {
		return chance;
	}
	
	public boolean rollChance() {
		if(chance == 100.0D) {
			return true;
		}
		
		Random random = new Random();
		Double randomDouble = (random.nextDouble() * 100);
		
		if(randomDouble <= chance) {
			return true;
		}
		return false;
	}
	
	public Integer getQuantity() {
		Random random = new Random();
		Integer count = 0;
		
		for(int i = 0; i < amount; i++) {
			Integer randomInt = random.nextInt(100) + 1;
			
			if(randomInt <= quantityChance) {
				count++;
			}
		}
		
		return count;
	}

	public boolean isSpread() {
		return spread;
	}
	
}
