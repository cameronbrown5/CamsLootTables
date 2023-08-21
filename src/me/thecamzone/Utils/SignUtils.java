package me.thecamzone.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
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
		
		if(!sign.getLine(0).equalsIgnoreCase("[LootTable]")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] This sign is not a loot table sign.");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Sign location: (world: " + sign.getWorld().getName() + ") " + sign.getX() + ", " + sign.getY() + ", " + sign.getZ() + ".");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
			return false;
		}
		
		LootTable lootTable = LootTableHandler.getInstance().getLootTable(sign.getLine(1));
		if(lootTable == null) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] The loot table \"" + sign.getLine(1) + "\" does not exist.");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Sign location: (world: " + sign.getWorld().getName() + ") " + sign.getX() + ", " + sign.getY() + ", " + sign.getZ() + ".");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
			return false;
		}
		
		Location location = sign.getLocation();
		
		Block block = location.getBlock();
		
		BlockFace direction = null;
		if(block.getBlockData() instanceof org.bukkit.block.data.type.Sign) {
			org.bukkit.block.data.type.Sign sign2 = (org.bukkit.block.data.type.Sign) block.getBlockData();
			direction = sign2.getRotation();
			
//			Directional directional = (Directional) block.getBlockData();
//			direction = directional.getFacing();
		}
		
		location.getBlock().setType(Material.CHEST);
		
		block = location.getBlock();
		if(!(block.getState() instanceof Chest)) {
			return false;
		}
		
		Chest chest = (Chest) block.getState();
		
		if(block.getBlockData() instanceof Directional) {
			if(direction == null) {
			} else {
				Directional directional = (Directional) block.getBlockData();
				if(!(direction == BlockFace.EAST || direction == BlockFace.WEST || direction == BlockFace.NORTH || direction == BlockFace.SOUTH)) {
					direction = BlockFace.SOUTH;
				}
				
				directional.setFacing(direction);
				block.setBlockData(directional);
				block.getState().update();
			}
		}
		
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
							if(availableSlots.isEmpty())
								break;
							
							inventory.setItem(availableSlots.get(0), item);
							availableSlots.remove(0);
						}
					}
				}
	        }

	    }.runTaskAsynchronously(CamsLootTables.getInstance());
		
		return true;
	}
	
}
