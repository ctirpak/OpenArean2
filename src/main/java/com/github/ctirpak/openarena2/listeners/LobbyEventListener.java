package com.github.ctirpak.openarena2.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.ctirpak.openarena2.ArenaGame;
import com.github.ctirpak.openarena2.ArenaPlayer;
import com.github.ctirpak.openarena2.OpenArena;

public final class LobbyEventListener implements Listener {
	private OpenArena plugin;
	private ArenaGame ag;

	public LobbyEventListener(OpenArena plugin) {
		this.plugin = plugin;
		ag = OpenArena.getCurrentArenaGame();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void LobbyItemInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (ag.isActive() || ag.hasPlayer(player) == -1)
			// the game is either not in lobby phase or the player is not in the game
			return;

		ArenaPlayer arenaPlayer = ag.getArenaPlayer(player);
		ItemStack notReady = plugin.getReadyItems()[0];
		ItemStack ready = plugin.getReadyItems()[1];

		ItemStack item = e.getItem();

		if (item.equals(notReady)) {
			player.sendMessage("You are ready!");
			arenaPlayer.ready();
			player.getInventory().setItemInHand(ready);

		} else if (item.equals(ready)) {
			player.sendMessage("You are not ready!");
			arenaPlayer.notReady();
			player.getInventory().setItemInHand(notReady);
		}

		e.setCancelled(true);

	}
}
