package me.thecamzone.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.thecamzone.Menus.LootTablesMenu;

public class CommandEditor extends LootTableCommand {
	public CommandEditor() {
		setName("editor");
		setInfoMessage("Opens the loot table editor.");
		setPermission("camsloottables.editor");
		setArgumentLength(1);
		setUsageMessage("/lt editor [LootTableName]");
		setUniversalCommand(true);
		setConsoleCommand(false);
	}

	public void execute(CommandSender sender, String[] args) {
		LootTablesMenu.openMenu((Player) sender, 0);
	}
}
