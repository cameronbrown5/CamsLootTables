package me.thecamzone.Utils;

import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class StringUtil {
	public static String formatColor(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static String capitalize(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(0, 1).toUpperCase();
	}

	public static String formatEnum(Enum<?> enumeration) {
		String[] split = enumeration.name().split("_");
		if (split.length > 0) {
			for (int i = 0; i < split.length; i++) {
				String word = split[i];
				if (word.equalsIgnoreCase("of") || word.equalsIgnoreCase("the")) {
					split[i] = word.toLowerCase();
				} else {
					split[i] = capitalize(word);
				}
			}
			return String.join(" ", (CharSequence[]) split);
		}
		return capitalize(enumeration.name());
	}

	public static String millisToText(long millis) {
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= days * TimeUnit.DAYS.toMillis(1L);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= hours * TimeUnit.HOURS.toMillis(1L);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= minutes * TimeUnit.MINUTES.toMillis(1L);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		if (days > 0L)
			return String.format("%s days %s hours %s minutes %s seconds", new Object[] { Long.valueOf(days),
					Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds) });
		if (hours > 0L)
			return String.format("%s hours %s minutes %s seconds",
					new Object[] { Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds) });
		if (minutes > 0L)
			return String.format("%s minutes %s seconds",
					new Object[] { Long.valueOf(minutes), Long.valueOf(seconds) });
		return "" + seconds + " seconds";
	}

	public static Integer parseInteger(CommandSender commandSender, String string) {
		try {
			return Integer.valueOf(Integer.parseInt(string));
		} catch (Exception e) {
			Messager.sendErrorMessage(commandSender, "&cYou must enter a numeric value with no decimals.");
			return null;
		}
	}

	public static Double parseDouble(CommandSender commandSender, String string) {
		try {
			return Double.valueOf(Double.parseDouble(string));
		} catch (Exception e) {
			Messager.sendErrorMessage(commandSender, "&cYou must enter a numeric value.");
			return null;
		}
	}
}
