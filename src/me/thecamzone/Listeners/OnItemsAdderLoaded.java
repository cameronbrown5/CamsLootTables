package me.thecamzone.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import me.thecamzone.CamsLootTables;

public class OnItemsAdderLoaded implements Listener {

	@EventHandler
	public void onItemsAdderLoaded(ItemsAdderLoadDataEvent e) {
		CamsLootTables.getInstance().loadHandlers();
	}
	
}
