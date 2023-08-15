package me.thecamzone.Commands;

import org.bukkit.command.CommandSender;

import me.thecamzone.LootTables.LootTableHandler;
import me.thecamzone.Utils.DataFile;
import me.thecamzone.Utils.Messager;
import net.md_5.bungee.api.ChatColor;

public class CommandReload extends LootTableCommand {
	public CommandReload() {
		setName("reload");
		setInfoMessage("Reloads the server files.");
		setPermission("camsloottables.admin");
		setArgumentLength(1);
		setUsageMessage("/lt reload");
		setUniversalCommand(true);
		setConsoleCommand(false);
	}

	public void execute(CommandSender sender, String[] args) {
		DataFile.reload();
		LootTableHandler.getInstance().load();
		Messager.sendSuccessMessage(sender, ChatColor.GREEN + "Successfully reloaded CamsLootTables.");
	}
}
