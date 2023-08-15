package me.thecamzone.LootTables;

import java.util.Random;

import org.bukkit.inventory.ItemStack;

public class ItemStackChance {

	private ItemStack item;
	private Double chance;
	
	public ItemStackChance(ItemStack item, Double chance) {
		this.item = item;
		this.chance = chance;
	}

	public ItemStack getItem() {
		return item;
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
	
}
