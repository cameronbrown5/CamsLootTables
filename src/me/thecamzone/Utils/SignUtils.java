package me.thecamzone.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;

import me.thecamzone.LootTables.ItemStackChance;
import me.thecamzone.LootTables.LootTable;
import me.thecamzone.LootTables.LootTableHandler;

public class SignUtils {

	public static boolean replaceSign(Sign sign) {
		if(sign == null) {
			return false;
		}
		
		if(sign.getLine(0).equalsIgnoreCase("[LootTable]")) {
			LootTable lootTable = LootTableHandler.getInstance().getLootTable(sign.getLine(1));
			if(lootTable == null) {
				Bukkit.getConsoleSender().sendMessage("loot table is null");
				return false;
			}
			
			Location location = sign.getLocation();
			location.getBlock().setType(Material.CHEST);
			
			Block block = location.getBlock();
			if(!(block.getState() instanceof Chest)) {
				Bukkit.getConsoleSender().sendMessage("block is not a chest");
				return false;
			}
			
			Chest chest = (Chest) block.getState();
			
			for(ItemStackChance itemStackChance : lootTable.getItems()) {
				if(!itemStackChance.rollChance()) {
					continue;
				}
				
				Inventory inventory = chest.getInventory();
				
				List<Integer> availableSlots = new ArrayList<>();
				for(int i = 0; i < inventory.getSize(); i++) {
					if(inventory.getItem(i) == null) {
						availableSlots.add(i);
					}
				}
				
				Collections.shuffle(availableSlots);
				
				inventory.setItem(availableSlots.get(0), itemStackChance.getItem());
			}
			
			return true;
		}
		return false;
	}
	
}
