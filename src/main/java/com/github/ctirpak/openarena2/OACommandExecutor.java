package com.github.ctirpak.openarena2;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OACommandExecutor implements CommandExecutor {
	private final OpenArena plugin;
	private Player player;

	public OACommandExecutor(OpenArena plugin) {
		// Store the plugin in situations where you need it.
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!cmd.getName().equalsIgnoreCase("oa") || args.length < 1)
			return false;

		switch (args[0]) {
		case "join":
			player = (Player) sender;
			if (!OpenArena.getCurrentArenaGame().addPlayer(player))
				player.sendMessage(ChatColor.RED + "You have already joined the game!");

			break;

		case "leave":
			player = (Player) sender;
			if (!OpenArena.getCurrentArenaGame().removePlayer(player))
				player.sendMessage(ChatColor.RED + "You are not in this game!");

			break;

		default:
			sender.sendMessage(ChatColor.RED + "Unknown Command!");
			break;
		}

		return true;
	}

}
