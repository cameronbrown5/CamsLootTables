package me.thecamzone.Utils;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

import me.thecamzone.CamsLootTables;

public class LogUtils {
	protected static final String PLUGIN_NAME = CamsLootTables.getInstance().getDescription().getName();

	protected static final Logger LOGGER = Bukkit.getLogger();

	public static void sendInfoLog(String text) {
		LOGGER.info("[" + PLUGIN_NAME + "] " + text);
	}

	public static void sendWarnLog(String text) {
		LOGGER.warning("[" + PLUGIN_NAME + "] " + text);
	}
}
