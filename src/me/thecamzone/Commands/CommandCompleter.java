package me.thecamzone.Commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.thecamzone.CamsLootTables;

public class CommandCompleter implements TabCompleter {

	private CamsLootTables plugin;
	
	public CommandCompleter(CamsLootTables plugin) {
		this.plugin = plugin;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> arguments = new ArrayList<>();
		Collection<LootTableCommand> subcommands = this.plugin.getSubcommands().values();
		for (LootTableCommand reaperCommand : subcommands) {
			String permission = reaperCommand.getPermission();
			if (!permission.isEmpty() && !sender.hasPermission(reaperCommand.getPermission()))
				continue;
			arguments.add(reaperCommand.getName());
		}
		LootTableCommand subcommand = (LootTableCommand) this.plugin.getSubcommands().get(args[0]);
		if (args.length > 1 && (subcommand == null || !sender.hasPermission(subcommand.getPermission()))) {
			arguments.clear();
			return arguments;
		}
		
		arguments.clear();
		return arguments;
	}

	private ArrayList<String> getCompletion(ArrayList<String> arguments, String[] args, int index) {
		ArrayList<String> results = new ArrayList<>();
		for (String argument : arguments) {
			if (!argument.toLowerCase().startsWith(args[index].toLowerCase()))
				continue;
			results.add(argument);
		}
		return results;
	}
}

