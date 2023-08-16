package me.thecamzone.Utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.thecamzone.CamsLootTables;

public class DataFile {

	private static File file;
	private static FileConfiguration customFile;
	
	// Generates file if not created.
	public static void setup()
	{
		file = new File(CamsLootTables.getInstance().getDataFolder(), "data.yml");
		
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch (IOException e)
			{
				Bukkit.getServer().getConsoleSender().sendMessage("Could not create data.yml.");
			}
			
		}
		
		customFile = YamlConfiguration.loadConfiguration(file);
	}
	
	public static FileConfiguration get()
	{
		return customFile;
	}
	
	public static void save()
	{
		try
		{
			customFile.save(file);
		} catch (IOException e)
		{
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save data.yml.");
		}
		
	}
	
	public static void reload()
	{
		customFile = YamlConfiguration.loadConfiguration(file);
	}
	
}
