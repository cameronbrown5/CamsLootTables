package me.thecamzone;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

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
import me.thecamzone.Rarities.RarityHandler;
import me.thecamzone.Rarities.RarityItemHandler;
import me.thecamzone.Utils.LootTablesFile;

public class CamsLootTables extends JavaPlugin {

	private static CamsLootTables plugin;
	private final Map<String, LootTableCommand> subcommands = new LinkedHashMap<>();
	
	private final NamespacedKey RARITY_ITEM_KEY = new NamespacedKey(this, "rarity_item");
	
	private CommandCompleter commandCompleter;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		LootTablesFile.setup();
		
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
	
	public NamespacedKey getRarityItemKey() {
		return RARITY_ITEM_KEY;
	}
	
	public void loadHandlers() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){

            @Override
            public void run() {
            	RarityHandler.getInstance().load();
        		RarityItemHandler.getInstance().load();
        		LootTableHandler.getInstance().load();
        		LootTableHandler.getInstance().loadInheritance();
            }
        },1L);
	}
	
	public Map<String, LootTableCommand> getSubcommands() {
		return subcommands;
	}

	public CommandCompleter getCommandCompleter() {
		return commandCompleter;
	}
	
}
