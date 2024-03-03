package me.thecamzone.Listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import de.tr7zw.nbtapi.NBTItem;
import me.thecamzone.CamsLootTables;
import me.thecamzone.LootTables.ItemStackChance;
import me.thecamzone.LootTables.LootTable;
import me.thecamzone.LootTables.LootTableHandler;
import me.thecamzone.Utils.Messager;
import me.thecamzone.Utils.BlockUtils;
import net.md_5.bungee.api.ChatColor;

public class OnPlayerInteract implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();
		Action action = e.getAction();
		ItemStack item = e.getItem();
		
		if(item == null) {
			return;
		}

		handleDebugStick(e);
		handleLootBags(e);
	}
	
	private boolean dropItems(Player player, LootTable lootTable) {
		List<ItemStack> items = new ArrayList<>();
		
		for(ItemStackChance itemStackChance : lootTable.getItems()) {
			if(!itemStackChance.rollChance()) {
				continue;
			}
			
			ItemStack item = itemStackChance.getItem();
			
			if(item == null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Failed to parse arguments for an item.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				return false;
			}
			
			Integer quantity = itemStackChance.getQuantity();
			if(!itemStackChance.isSpread()) {
				item.setAmount(quantity);
				
				items.add(item);
			}
			else {
				for(int i = 0; i < quantity; i++) {
					items.add(item);
				}
			}
		}
		
		World world = player.getWorld();
		Location location = player.getLocation();
		
		for(ItemStack itemStack : items) {
			world.dropItemNaturally(location, itemStack);
		}
		
		return true;
	}

	public void handleDebugStick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		ItemStack item = e.getItem();
		Action action = e.getAction();
		Block block = e.getClickedBlock();

		if(item.getType() == Material.STICK) {
			if(!player.hasPermission("camsloottables.admin")) {
				return;
			}

			if(!item.hasItemMeta()) {
				return;
			}

			if(!item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CamsLootTables.getInstance(), "camsloottables"), PersistentDataType.STRING).equalsIgnoreCase("debug_stick")) {
				return;
			}

			if(action != Action.RIGHT_CLICK_BLOCK) {
				return;
			}

			if(block == null) {
				return;
			}

			if(!(block.getState() instanceof Sign)) {
				return;
			}

			e.setCancelled(true);

			Sign sign = (Sign) block.getState();

			if(BlockUtils.replaceSign(sign, true)) {
				Messager.sendSuccessMessage(player, ChatColor.GREEN + "Successfully created a LootTable chest.");
			} else {
				Messager.sendErrorMessage(player, ChatColor.RED + "Could not create a LootTable chest. Check console for information.");
			}
		}
	}

	public void handleLootBags(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		ItemStack item = e.getItem();
		Action action = e.getAction();
		Block block = e.getClickedBlock();

		NBTItem nbti = new NBTItem(item);

		if(nbti.hasTag("lootbag")) {
			if(!(action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR)) {
				return;
			}

			if(nbti.getString("lootbag") == null) {
				return;
			}

			LootTable lootTable = LootTableHandler.getInstance().getLootTable(nbti.getString("lootbag"));
			if(lootTable == null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] The loot table \"" + nbti.getString("lootbag") + "\" does not exist.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] For loot bag item: \"" + nbti.getName() + "\".");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				return;
			}

			e.setCancelled(true);

			if(!dropItems(player, lootTable)) {
				if(player.hasPermission("camsloottables.admin")) {
					Messager.sendErrorMessage(player, ChatColor.RED + "An error occurred while opening this loot bag. Check console for errors.");
				} else {
					Messager.sendErrorMessage(player, ChatColor.RED + "An error occurred while opening this loot bag. Please contact an admin for help.");
				}
			} else {
				item.setAmount(item.getAmount() - 1);
			}
		}
	}

}
