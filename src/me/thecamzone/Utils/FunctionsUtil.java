package me.thecamzone.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;

public class FunctionsUtil {

public static Integer parseIntegerFunction(String functionString) {

		if(functionString == null) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Integer function is null.");
			return null;
		}

		functionString = functionString.replace(")", "");
		String[] args = functionString.split("\\(");

		if(args.length == 0) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Integer function is an empty string.");
			return null;
		}

		String functionName = args[0];

		if(functionName.equalsIgnoreCase("range")) {
			if(args.length <= 1) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Incorrect syntax for \"" + functionString + "\").");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Correct syntax: \"range(BottomNumber,TopNumber)\".");
				return null;
			}

			String[] inputNumbers = args[1].split("\\,");

			Integer bottomNumber = null;
			try {
				bottomNumber = Integer.parseInt(inputNumbers[0]);
			} catch (NumberFormatException e) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Incorrect function for \"" + functionString + "\").");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + inputNumbers[0] + "\" is not a valid integer.");
				return null;
			}

			Integer topNumber = null;
			try {
				topNumber = Integer.parseInt(inputNumbers[1]);
			} catch (NumberFormatException e) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Incorrect function for \"" + functionString + "\").");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + inputNumbers[1] + "\" is not a valid integer.");
				return null;
			}

			Random random = new Random();

			Integer difference = topNumber - bottomNumber;
			Integer randomInt = random.nextInt(difference + 1);

			randomInt += bottomNumber;
			return randomInt;
		}

		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Unknown argument \"" + functionName + "\".");
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Available arguments: random");
		return null;
	}

	public static String parseStringFunction(String functionString) {

		if(functionString == null) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] String function is null.");
			return null;
		}

		functionString = functionString.replace(")", "");
		String[] args = functionString.split("\\(");

		if(args.length == 0) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] String function is an empty string.");
			return null;
		}

		String functionName = args[0];

		if(functionName.equalsIgnoreCase("random")) {
			if(args.length <= 1) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Incorrect syntax for \"" + functionString + "\").");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Correct syntax: \"random(RandomThing1,RandomThing2...)\".");
				return null;
			}

			String[] randomArray = args[1].split("\\,");

			List<String> randomList = Arrays.asList(randomArray);

			Random random = new Random();

			Integer randomInt = random.nextInt(randomList.size());

			return randomList.get(randomInt);
		} else if(functionName.equalsIgnoreCase("chance")) {
			if(args.length <= 1) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Incorrect syntax for \"" + functionString + "\").");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Correct syntax: \"chance(RandomThing1:Chance,RandomThing2:Chance...)\".");
				return null;
			}

			String[] chanceArray = args[1].split("\\,");

			HashMap<String, Integer> itemPercentages = new HashMap<>();
			for(String itemChance : chanceArray) {
				String[] item = itemChance.split("\\:");

				String namespacedItem = null;
				if(item.length == 2) {
					namespacedItem = item[0];
				} else if(item.length == 3) {
					namespacedItem = item[0] + ":" + item[1];
				}

				if(namespacedItem == null) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Incorrect function for \"" + functionString + "\").");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Incorrect syntax for chanced function.");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Correct syntax for chance function input: namespacedInput:percentChance");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Example: chance(INFINITY:20,KNOCKBACK:80)");
					return null;
				}

				String chanceString;
				try {
					chanceString = item[item.length - 1];
				} catch (IndexOutOfBoundsException e) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Incorrect function for \"" + functionString + "\").");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Incorrect syntax for chanced function.");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Correct syntax for chance function input: namespacedInput:percentChance");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Example: chance(INFINITY:20,KNOCKBACK:80)");
					return null;
				}

				Integer chance;
				try {
					chance = Integer.parseInt(chanceString);
				} catch (NumberFormatException e) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Incorrect function for \"" + functionString + "\").");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] \"" + chanceString + "\" is not a valid integer.");
					return null;
				}

				itemPercentages.put(namespacedItem, chance);
			}

			List<String> chanceList = new ArrayList<>();
			for(String s : itemPercentages.keySet()) {
				for(int i = 0; i < itemPercentages.get(s); i++) {
					chanceList.add(s);
				}
			}

			if(chanceList.size() != 100) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Incorrect function for \"" + functionString + "\").");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Chances must add up to 100.");
				return null;
			}

			Random random = new Random();

			Integer randomInt = random.nextInt(chanceList.size());

			String returnString = chanceList.get(randomInt);
			chanceList.clear();
			return returnString;
		}

		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Unknown argument \"" + functionName + "\".");
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CamsLootTables] Available arguments: random | chance | range");
		return null;
	}

}
