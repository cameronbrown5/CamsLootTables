package me.thecamzone.Commands;

import org.bukkit.command.CommandSender;

public abstract class LootTableCommand {
	private String name;

	private String infoMessage;

	private String permission;

	private String usageMessage;

	private int argumentLength;

	private boolean isConsoleCommand;

	private boolean isPlayerCommand;

	private boolean isUniversalCommand;

	public abstract void execute(CommandSender paramCommandSender, String[] paramArrayOfString);

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfoMessage() {
		return this.infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public String getPermission() {
		return this.permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public int getArgumentLength() {
		return this.argumentLength;
	}

	public void setArgumentLength(int argumentLength) {
		this.argumentLength = argumentLength;
	}

	public String getUsageMessage() {
		return this.usageMessage;
	}

	public void setUsageMessage(String usageMessage) {
		this.usageMessage = usageMessage;
	}

	public boolean isConsoleCommand() {
		return this.isConsoleCommand;
	}

	public void setConsoleCommand(boolean isConsoleCommand) {
		this.isConsoleCommand = isConsoleCommand;
	}

	public boolean isPlayerCommand() {
		return this.isPlayerCommand;
	}

	public void setPlayerCommand(boolean isPlayerCommand) {
		this.isPlayerCommand = isPlayerCommand;
	}

	public boolean isUniversalCommand() {
		return this.isUniversalCommand;
	}

	public void setUniversalCommand(boolean isUniversalCommand) {
		this.isUniversalCommand = isUniversalCommand;
	}
}
