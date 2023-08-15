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
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.thecamzone.CamsLootTables;
import me.thecamzone.LootTables.ItemStackChance;
import me.thecamzone.LootTables.LootTable;
import me.thecamzone.LootTables.LootTableHandler;
import net.md_5.bungee.api.ChatColor;

public class SignUtils {

	public static boolean replaceSign(Sign sign) {
		if(sign == null) {
			return false;
		}
		
		if(sign.getLine(0).equalsIgnoreCase("[LootTable]")) {
			LootTable lootTable = LootTableHandler.getInstance().getLootTable(sign.getLine(1));
			if(lootTable == null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] The loot table \"" + sign.getLine(1) + "\" does not exist.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Sign location: (world: " + sign.getWorld() + ") " + sign.getX() + ", " + sign.getY() + ", " + sign.getZ() + ".");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				return false;
			}
			
			Location location = sign.getLocation();
			location.getBlock().setType(Material.CHEST);
			
			Block block = location.getBlock();
			if(!(block.getState() instanceof Chest)) {
				return false;
			}
			
			Chest chest = (Chest) block.getState();
			
			new BukkitRunnable() {
		        @Override
		        public void run() {
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
						
						ItemStack item = itemStackChance.getItem();
						Integer quantity = itemStackChance.getQuantity();
						if(!itemStackChance.isSpread()) {
							item.setAmount(quantity);
							
							itemStackChance.setItem(item);
							
							inventory.setItem(availableSlots.get(0), item);
						}
						else {
							for(int i = 0; i < quantity; i++) {
								inventory.setItem(availableSlots.get(0), item);
								availableSlots.remove(0);
							}
						}
					}
		        }

		    }.runTaskAsynchronously(CamsLootTables.getInstance());
			
			return true;
		}
		return false;
	}
	
}
