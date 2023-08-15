package me.thecamzone.Commands;

import org.bukkit.command.CommandSender;

import me.thecamzone.Utils.Messager;

public class CommandHelp extends LootTableCommand {
	public CommandHelp() {
		setName("help");
		setInfoMessage("Displays this list.");
		setPermission("loottable.help");
		setArgumentLength(1);
		setUsageMessage("/lt help");
		setUniversalCommand(true);
	}

	public void execute(CommandSender sender, String[] args) {
		Messager.sendHelpMessage(sender);
	}
}
