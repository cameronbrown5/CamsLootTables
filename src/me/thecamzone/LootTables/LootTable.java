package me.thecamzone.LootTables;

import java.util.ArrayList;
import java.util.List;

public class LootTable {

	private String displayName;
	
	private List<ItemStackChance> items;
	//private List<LootTableGroup> groups = new ArrayList<LootTableGroup>();
	
	public LootTable(String displayName) {
		this.displayName = displayName;
		this.items = new ArrayList<ItemStackChance>();
	}
	
	public void addItem(ItemStackChance item) {
		if(!items.contains(item))
			items.add(item);
	}

	public List<ItemStackChance> getItems() {
		return items;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
}
