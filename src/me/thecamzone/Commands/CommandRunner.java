package me.thecamzone.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.thecamzone.CamsLootTables;
import me.thecamzone.Utils.Messager;

public class CommandRunner implements CommandExecutor {
	private CamsLootTables plugin;
	
	public CommandRunner() {
		this.plugin = CamsLootTables.getInstance();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase("loottable"))
			return false;
		if (args.length < 1) {
			Messager.sendHelpMessage(sender);
			return true;
		}
		String inputCommand = args[0].toLowerCase();
		if (!this.plugin.getSubcommands().containsKey(inputCommand)) {
			Messager.sendErrorMessage(sender,
					"&cUnknown command. Type &l/lt help &cto see the full command list.");
			return true;
		}
		LootTableCommand subcommand = (LootTableCommand) this.plugin.getSubcommands().get(inputCommand);
		if (subcommand.isPlayerCommand() && !(sender instanceof org.bukkit.entity.Player)) {
			Messager.sendErrorMessage(sender, "&cNot available for consoles.");
			return true;
		}
		if (subcommand.isConsoleCommand() && sender instanceof org.bukkit.entity.Player) {
			Messager.sendErrorMessage(sender, "&cNot available for players.");
			return true;
		}
		if (args.length < subcommand.getArgumentLength()) {
			Messager.sendErrorMessage(sender, "&cUsage: &l" + subcommand.getUsageMessage());
			return true;
		}
		String permission = subcommand.getPermission();
		if (!permission.isEmpty() && !sender.hasPermission(subcommand.getPermission())) {
			Messager.sendNoPermissionMessage(sender);
			return true;
		}
		subcommand.execute(sender, args);
		return true;
	}
}
