package me.thecamzone.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.thecamzone.Menus.LootTablesMenu;
import me.thecamzone.Utils.ItemFactory;

public class CommandDebug extends LootTableCommand {
	public CommandDebug() {
		setName("debug");
		setInfoMessage("Gives a debug stick.");
		setPermission("camsloottables.admin");
		setArgumentLength(1);
		setUsageMessage("/lt debug");
		setUniversalCommand(true);
		setConsoleCommand(false);
	}

	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		player.getInventory().addItem(ItemFactory.getDebugItem());
	}
}
