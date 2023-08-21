package me.thecamzone.Listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import me.thecamzone.CamsLootTables;
import me.thecamzone.Utils.Messager;
import me.thecamzone.Utils.SignUtils;
import net.md_5.bungee.api.ChatColor;

public class OnPlayerInteract implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();
		Action action = e.getAction();
		ItemStack item = e.getItem();
		
		if(player == null) {
			return;
		}
		
		if(!player.hasPermission("camsloottables.admin")) {
			return;
		}
		
		if(item == null) {
			return;
		}
		
		if(item.getType() != Material.STICK) {
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
		
		if(SignUtils.replaceSign(sign)) {
			Messager.sendSuccessMessage(player, ChatColor.GREEN + "Successfully created a LootTable chest.");
		} else {
			Messager.sendErrorMessage(player, ChatColor.RED + "Could not create a LootTable chest. Check console for information.");
		}
	}
	
}
