package me.thecamzone;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.ipvp.canvas.MenuFunctionListener;

import me.thecamzone.Commands.CommandCompleter;
import me.thecamzone.Commands.CommandDebug;
import me.thecamzone.Commands.CommandEditor;
import me.thecamzone.Commands.CommandHelp;
import me.thecamzone.Commands.CommandReload;
import me.thecamzone.Commands.CommandRunner;
import me.thecamzone.Commands.LootTableCommand;
import me.thecamzone.Listeners.OnChunkGeneration;
import me.thecamzone.Listeners.OnInventoryInteract;
import me.thecamzone.Listeners.OnItemsAdderLoaded;
import me.thecamzone.Listeners.OnPlayerInteract;
import me.thecamzone.LootTables.LootTableHandler;
import me.thecamzone.Utils.DataFile;

public class CamsLootTables extends JavaPlugin {

	private static CamsLootTables plugin;
	private final Map<String, LootTableCommand> subcommands = new LinkedHashMap<>();
	
	private CommandCompleter commandCompleter;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		DataFile.setup();
		
		configureFiles();
		registerCommands();
		registerListeners();
		
		commandCompleter = new CommandCompleter(this);
	}
	
	public static CamsLootTables getInstance() {
		return plugin;
	}

	private void configureFiles() {
		saveDefaultConfig();
	}
	
	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new OnChunkGeneration(), this);
		getServer().getPluginManager().registerEvents(new OnItemsAdderLoaded(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerInteract(), this);
		getServer().getPluginManager().registerEvents(new OnInventoryInteract(), this);
		//getServer().getPluginManager().registerEvents(new MenuFunctionListener(), plugin);
	}
	
	private void registerCommands() {
		this.subcommands.put("reload", new CommandReload());
		this.subcommands.put("debug", new CommandDebug());
		this.subcommands.put("editor", new CommandEditor());
		this.subcommands.put("help", new CommandHelp());
		getCommand("loottable").setExecutor((CommandExecutor) new CommandRunner());
		getCommand("loottable").setTabCompleter((TabCompleter) new CommandCompleter(this));
	}
	
	public Map<String, LootTableCommand> getSubcommands() {
		return subcommands;
	}

	public CommandCompleter getCommandCompleter() {
		return commandCompleter;
	}
	
}
