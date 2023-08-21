package me.thecamzone.Utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.thecamzone.CamsLootTables;

public class LootTablesFile {

	private static String fileName = "loottables.yml";
	private static File file;
	private static FileConfiguration customFile;
	
	private static CamsLootTables plugin;
	
	// Generates file if not created.
	public static void setup() {
		plugin = CamsLootTables.getInstance();
		
		saveDefaultConfig();
		
		customFile = YamlConfiguration.loadConfiguration(file);
	}
	
	public static FileConfiguration get() {
		if(customFile == null) {
			setup();
		}
		
		return customFile;
	}
	
	public static void save() {
		try {
			customFile.save(file);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save " + fileName);
		}
	}
	
	public static void reload() {
		customFile = YamlConfiguration.loadConfiguration(file);
	}
	
	public static void saveDefaultConfig() {
		if (file == null)
			file = new File(plugin.getDataFolder(), fileName);
		if (!file.exists())
			plugin.saveResource(fileName, false);
	}
	
}
