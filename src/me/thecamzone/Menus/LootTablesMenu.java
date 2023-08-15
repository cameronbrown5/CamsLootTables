package me.thecamzone.Menus;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.paginate.PaginatedMenuBuilder;
import org.ipvp.canvas.type.ChestMenu;

public class LootTablesMenu {

	public static List<Menu> getMenu() {
		Menu.Builder pageTemplate = ChestMenu.builder(3)
				.title("Loot Tables")
				.redraw(true);
		
		Mask itemSlots = BinaryMask.builder(pageTemplate.getDimensions())
		        .pattern("011111110")
		        .build();
		
		List<Menu> pages = PaginatedMenuBuilder.builder(pageTemplate)
		        .slots(itemSlots)
		        .nextButton(new ItemStack(Material.ARROW))
		        .nextButtonEmpty(new ItemStack(Material.AIR)) // Icon when no next page available
		        .nextButtonSlot(23)
		        .previousButton(new ItemStack(Material.ARROW))
		        .previousButtonEmpty(new ItemStack(Material.AIR)) // Icon when no previous page available
		        .previousButtonSlot(21)
		        .addItem(new ItemStack(Material.DIRT))
		        .addItem(new ItemStack(Material.GRASS))
		        .addItem(new ItemStack(Material.COBBLESTONE))
		        .addItem(new ItemStack(Material.STONE))
		        .build();
		return pages;
	}

	public static void openMenu(Player player, Integer page) {
		
	}
}
