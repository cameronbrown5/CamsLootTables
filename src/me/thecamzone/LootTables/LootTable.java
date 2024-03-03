package me.thecamzone.LootTables;

import java.util.ArrayList;
import java.util.List;

public class LootTable {

	private String displayName;
	
	private List<LootTable> inherits;
	
	private List<ItemStackChance> items;
	//private List<LootTableGroup> groups = new ArrayList<LootTableGroup>();
	
	public LootTable(String displayName) {
		this.displayName = displayName;
		this.inherits = new ArrayList<>();
		this.items = new ArrayList<ItemStackChance>();
	}
	
	public void addItem(ItemStackChance item) {
		if(!items.contains(item))
			items.add(item);
	}

	public List<ItemStackChance> getItems() {
		List<ItemStackChance> itemStackChances = items;
		
		List<LootTable> checkedLootTables = new ArrayList<>();
		
		for(LootTable inheritedLootTable : inherits) {
			if(inheritedLootTable == this) {
				continue;
			}
			
			if(checkedLootTables.contains(inheritedLootTable)) {
				continue;
			}
			
			for(ItemStackChance itemStackChance : inheritedLootTable.getItems()) {
				if(!itemStackChances.contains(itemStackChance)) {
					itemStackChances.add(itemStackChance);
				}
			}
			
			checkedLootTables.add(inheritedLootTable);
		}
		
		return itemStackChances;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public List<LootTable> getInherits() {
		return inherits;
	}
	
	public void addInheritedLootTable(LootTable lootTable) {
		if(!inherits.contains(lootTable)) {
			inherits.add(lootTable);
		}
	}
	
}
