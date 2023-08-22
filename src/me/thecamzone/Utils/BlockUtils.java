package me.thecamzone.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Chest.Type;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.thecamzone.CamsLootTables;
import me.thecamzone.LootTables.ItemStackChance;
import me.thecamzone.LootTables.LootTable;
import me.thecamzone.LootTables.LootTableHandler;
import net.md_5.bungee.api.ChatColor;

public class BlockUtils {

	public static BlockFace getDirection(Block block) {
		if(block.getBlockData() instanceof org.bukkit.block.data.type.Sign) {
			org.bukkit.block.data.type.Sign sign = (org.bukkit.block.data.type.Sign) block.getBlockData();
			return sign.getRotation();
		} else {
			return BlockFace.NORTH;
		}
	}
	
	public static void setDirection(Block block, BlockFace direction) {
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
	}
	
	public static Inventory getBlockInventory(BlockState blockState) {
		if(blockState instanceof Container) {
			return ((Container) blockState).getInventory();
		} else {
			return null;
		}
	}
	
	public static List<Block> getNearbyBlocks(Block block) {
		Location loc = block.getLocation();
		
		Block north = loc.clone().subtract(0, 0, 1).getBlock();
		Block south = loc.clone().add(0, 0, 1).getBlock();
		Block west = loc.clone().subtract(1, 0, 0).getBlock();
		Block east = loc.clone().add(1, 0, 0).getBlock();
		
//		for(Block b : Arrays.asList(north, south, west, east)) {
//			Bukkit.getConsoleSender().sendMessage("================");
//			Bukkit.getConsoleSender().sendMessage(b.getX() + "");
//			Bukkit.getConsoleSender().sendMessage(b.getZ() + "");
//			Bukkit.getConsoleSender().sendMessage("================");
//		}
		
		return Arrays.asList(north, south, west, east);
	}
	
	public static void spawnDoubleChest(Block block1, Block block2, BlockFace direction) {
        block1.setType(Material.CHEST);
        block2.setType(Material.CHEST);

        setDirection(block1, direction);
        setDirection(block2, direction);
        
        Chest chest1 = (Chest) block1.getState();
        Chest chest2 = (Chest) block2.getState();

        org.bukkit.block.data.type.Chest chestData1 = (org.bukkit.block.data.type.Chest) chest1.getBlockData();
        org.bukkit.block.data.type.Chest chestData2 = (org.bukkit.block.data.type.Chest) chest2.getBlockData();

        chestData1.setType(Type.LEFT);
        chest1.setBlockData(chestData1);
        
        chestData2.setType(Type.RIGHT);
        chest2.setBlockData(chestData2);

        chest1.update(true);
        chest2.update(true);
	}
	
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
		
		BlockFace direction = getDirection(sign.getBlock());
		
		String containerName = sign.getLine(2);
		Material containerMaterial = null;
		if(containerName.equalsIgnoreCase("DOUBLE_CHEST")) {
			Sign nearbySign = null;
			
			List<Block> blocksNearby = getNearbyBlocks(sign.getBlock());
			for(Block nearbyBlock : blocksNearby) {
				if(nearbyBlock.getState() instanceof Sign) {
					nearbySign = (Sign) nearbyBlock.getState();
					break;
				}
			}
			
			if(nearbySign == null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] LootTable specified type DOUBLE_CHEST, but did not specify where to put the other chest.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] To specify where to put the second chest, put an empty sign next to the [LootTable] sign.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Sign location: (world: " + sign.getWorld().getName() + ") " + sign.getX() + ", " + sign.getY() + ", " + sign.getZ() + ".");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				
				return false;
			}
			
			spawnDoubleChest(nearbySign.getBlock(), sign.getBlock(), direction);
			containerMaterial = Material.CHEST;
		}
		
		if(containerMaterial == null) {
			try {
				containerMaterial = Material.valueOf(containerName);
			} catch (IllegalArgumentException e) {
				if(containerName.length() > 0) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] The block \"" + containerName + "\" is not a valid block.");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Sign location: (world: " + sign.getWorld().getName() + ") " + sign.getX() + ", " + sign.getY() + ", " + sign.getZ() + ".");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				}
				containerMaterial = Material.CHEST;
			}
		}
		
		Location location = sign.getLocation();
		
		Block block = location.getBlock();
		
		if(block.getType() != Material.CHEST) {
			location.getBlock().setType(containerMaterial);
			
			block = location.getBlock();
		}
		
		if(!(block.getState() instanceof Container)) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] The block \"" + containerName + "\" is not an inventory block.");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Sign location: (world: " + sign.getWorld().getName() + ") " + sign.getX() + ", " + sign.getY() + ", " + sign.getZ() + ".");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
			return false;
		}
		
		BlockState blockState = block.getState();
		
		setDirection(block, direction);
		
    	for(ItemStackChance itemStackChance : lootTable.getItems()) {
			if(!itemStackChance.rollChance()) {
				continue;
			}
			
			Inventory inventory = getBlockInventory(blockState);
			
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
					
					inventory.setItem(availableSlots.get(0), itemStackChance.getItem());
					availableSlots.remove(0);
				}
			}
		}
		
		return true;
	}
	
}
