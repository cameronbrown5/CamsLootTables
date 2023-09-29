package me.thecamzone.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.willfp.ecoenchants.enchants.EcoEnchants;

import dev.lone.itemsadder.api.CustomStack;
import me.thecamzone.CamsLootTables;
import me.thecamzone.Rarities.Rarity;
import me.thecamzone.Rarities.RarityItem;
import me.thecamzone.Rarities.RarityItemHandler;
import net.md_5.bungee.api.ChatColor;

public class ItemFactory {

	public static ItemStack getDebugItem() {
		ItemStack item = new ItemStack(Material.STICK);
		ItemMeta itemMeta = item.getItemMeta();
		
		itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "LootTable Debug Stick");
		itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right click a sign to spawn" , ChatColor.GRAY + "a loot table chest."));
		itemMeta.getPersistentDataContainer().set(new NamespacedKey(CamsLootTables.getInstance(), "camsloottables"), PersistentDataType.STRING, "debug_stick");
		
		item.setItemMeta(itemMeta);
		return item;
	}
	
	public static String[] convertNamespacedID(String namespacedID) {
		String[] namespacedIDArray = namespacedID.split(":");
		String namespace;
		String materialName;
		
		if(namespacedIDArray.length == 2) {
			namespace = namespacedIDArray[0];
			materialName = namespacedIDArray[1];
		} else {
			namespace = "minecraft";
			materialName = namespacedIDArray[0];
		}
		
		String[] stringArray = {namespace, materialName};
		
		return stringArray;
	}
	
	public static ItemStack formatRarityItem(ItemStack item, Rarity rarity) {
		if(item == null) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Cannot format null item.");
			return null;
		}
		
		ItemStack localItem = item.clone();
		
		ItemMeta itemMeta = localItem.getItemMeta();
		List<String> lore = itemMeta.getLore();
		if (lore == null) {
			lore = new ArrayList<>();
		}
		
		if(lore.size() == 0) {
			lore.add(rarity.getDisplayName());
		} else {
			lore.add(0, "");
			lore.add(lore.size() - 1, "");
			lore.add(lore.size() - 1, rarity.getDisplayName());
			lore.add(lore.size() - 1, "");
			
//			if(lore.size() > 2) {
//				lore.add(2, "");
//			}
		}

		itemMeta.setLore(lore);
		localItem.setItemMeta(itemMeta);

		return localItem;
	}
	
	public static ItemStack parseArgs(RarityItem item) {
		ItemStack localItem = item.getItem().clone();
		
		for (String arg : item.getRarityArgs(item.getRarity())) {
			String[] args = arg.split(" ");
			if (args.length == 0) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(
						ChatColor.RED + "[CamsLootTables] Invalid rarity argument for \"" + item.getName() + "\" in items.yml.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Rarity argument given:");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + arg + "\"");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				continue;
			}

			if (args[1] == null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(
						ChatColor.RED + "[CamsLootTables] Invalid rarity argument for \"" + item.getName() + "\" in items.yml.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Rarity argument given:");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + arg + "\"");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				continue;
			}

			if (args[2] == null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(
						ChatColor.RED + "[CamsLootTables] Invalid syntax for args in \"" + item.getName() + "\" from items.yml.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Syntaxes:");
				Bukkit.getConsoleSender()
						.sendMessage(ChatColor.RED + "[CamsLootTables] \"enchant <NamespacedEnchantment> <Level>\"");
				Bukkit.getConsoleSender()
						.sendMessage(ChatColor.RED + "[CamsLootTables] \"attribute <Attribute> <+/-(Number)>\"");
				Bukkit.getConsoleSender()
						.sendMessage(ChatColor.RED + "[CamsLootTables] \"modifier <Modifier> <true/false>\"");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				continue;
			}

			if (args[0].equalsIgnoreCase("enchant")) {
				if(args[1].contains("(")) {
					String parsedString = FunctionsUtil.parseStringFunction(args[1]);
					if(parsedString == null) {
						return null;
					}
					
					args[1] = parsedString;
				}
				
				
				if(args[2].contains("(")) {
					Integer parsedInteger = FunctionsUtil.parseIntegerFunction(args[2]);
					if(parsedInteger == null) {
						return null;
					}
					
					args[2] = parsedInteger.toString();
				}
				Integer level;
				try {
					level = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					Bukkit.getConsoleSender().sendMessage(
							ChatColor.RED + "[CamsLootTables] Invalid integer for \"" + item.getName() + "\" in items.yml.");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Integer given: " + args[2]);
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
					continue;
				}

				localItem = ItemFactory.enchantItem(localItem, args[1], level);
			}
//			else if(args[0].equalsIgnoreCase("attribute")) {
//				Integer level;
//				try {
//					level = Integer.parseInt(args[2]);
//				} catch (NumberFormatException e) {
//					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
//					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Invalid integer for \"" + name + "\" in items.yml.");
//					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Integer given: " + args[2]);
//					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
//					continue;
//				}
//				
//				localItem = ItemFactory.enchantItem(localItem, args[1], level);
//			} 
			else {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(
						ChatColor.RED + "[CamsLootTables] Invalid rarity argument for \"" + item.getName() + "\" in items.yml.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Rarity argument given:");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + arg + "\"");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Valid argument types:");
				Bukkit.getConsoleSender()
						.sendMessage(ChatColor.RED + "[CamsLootTables] enchant | attribute | modifier");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				continue;
			}
		}
		return localItem;
	}
	
	public static ItemStack getItemStack(String namespace, String materialName) {
		ItemStack item = new ItemStack(Material.AIR);
		
		if(RarityItemHandler.getInstance().getRarityItems().containsKey(materialName.toLowerCase())) {
			return RarityItemHandler.getInstance().getRarityItem(materialName).getItem();
		}
		
		if(namespace.equalsIgnoreCase("minecraft")) {
			Material material = Material.matchMaterial(materialName);
			if(material == null) {
				return null;
			}
			
			item = new ItemStack(material);
		} else {
			CustomStack stack = CustomStack.getInstance(namespace.toLowerCase() + ":" + materialName.toLowerCase());
			if(stack == null) {
				return null;
			}
			
			item = stack.getItemStack();
		}
		
		return item;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack enchantItem(ItemStack item, String enchantmentName, Integer level) {
		if(enchantmentName.equalsIgnoreCase("none")) {
			return item;
		}
		
		String namespace;
		String enchantmentString;
		if(enchantmentName.contains(":")) {
			String[] namespacedEnchantment = enchantmentName.split(":");
			if(namespacedEnchantment.length != 2) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Could not enchant item.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Namespaced enchantment is invalid.");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
				return null;
			}
			
			namespace = namespacedEnchantment[0];
			enchantmentString = namespacedEnchantment[1];
		} else {
			namespace = "minecraft";
			enchantmentString = enchantmentName;
		}
		
		Enchantment enchantment = null;
		
		if(namespace.equalsIgnoreCase("minecraft")) {
			enchantment = Enchantment.getByName(enchantmentString);
		} else if(namespace.equalsIgnoreCase("ecoenchants")) {
			enchantment = EcoEnchants.getByID(enchantmentString.toLowerCase());
		}
		
		if(enchantment == null) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Could not enchant item.");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Could not find enchantment with name " + namespace + ":" + enchantmentString);
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-------------------------");
			return null;
		}
		
		if(item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			
			if(meta instanceof EnchantmentStorageMeta) {
				EnchantmentStorageMeta enchantmentMeta = (EnchantmentStorageMeta) meta;
				enchantmentMeta.addStoredEnchant(enchantment, level, true);
				item.setItemMeta(enchantmentMeta);
			}
		}
		
		item.addUnsafeEnchantment(enchantment, level);
		
		return item;
	}
	
}
