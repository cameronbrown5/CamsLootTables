package me.thecamzone.Rarities;

public class Rarity implements Comparable<Rarity> {

	private String name;
	private String displayName;
	private Integer chance;
	
	public Rarity(String name, String displayName, Integer chance) {
		this.name = name;
		this.displayName = displayName;
		this.chance = chance;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Integer getChance() {
		return chance;
	}

	@Override
	public int compareTo(Rarity o) {
		if(getChance() == null || o.getChance() == null) {
			return 0;
		}
		
		return getChance().compareTo(o.getChance());
	}
	
}
