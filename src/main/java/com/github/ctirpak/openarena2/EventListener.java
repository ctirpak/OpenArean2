package com.github.ctirpak.openarena2;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public final class EventListener implements Listener {
	private OpenArena plugin;

	public EventListener(OpenArena plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void playerMove(PlayerMoveEvent e) {
		if (!OpenArena.getCurrentArenaGame().isActive()) {
			e.getPlayer().setFoodLevel(20);
		}
		// Location from = e.getFrom();
		// Location to = e.getTo();
		//
		// if (to.getBlockX() == from.getBlockX() && to.getBlockZ() == from.getBlockZ())
		// return; // The player hasn't moved
		//
		// Player player = e.getPlayer();
		// player.sendMessage(ChatColor.RED + "You are not allowed to move!");
		// player.teleport(from);
	}

	@EventHandler
	public void playerInteract(PlayerInteractEvent e) {
		if (!OpenArena.getCurrentArenaGame().isActive()
				&& OpenArena.getCurrentArenaGame().playerInArena(e.getPlayer())) { 
			// Game isnt active and player is in the arena game
			ItemStack notReady = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1); // red
			ItemMeta notReadyMeta = notReady.getItemMeta();
			notReadyMeta.setDisplayName("Not Ready");
			notReady.setItemMeta(notReadyMeta);

			ItemStack ready = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1); // green
			ItemMeta readyMeta = ready.getItemMeta();
			readyMeta.setDisplayName("Ready");
			ready.setItemMeta(readyMeta);

			ItemStack item = e.getItem();
			Material itemMaterial = e.getMaterial();

			ItemMeta itemMeta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
			if (item.equals(notReady)) {
				e.getPlayer().sendMessage(ChatColor.GREEN + "You are ready!");
				itemMeta.setDisplayName("Ready");
				e.getPlayer().getInventory().getItemInMainHand().setItemMeta(itemMeta);

				e.getPlayer().getInventory().setItemInMainHand(ready);
				OpenArena.getCurrentArenaGame().getArenaPlayer(e.getPlayer()).ready();

				// see if everybody is ready
				for (ArenaPlayer player : OpenArena.getCurrentArenaGame().getPlayers()) {
					if (!player.isReady())
						return;
				}
				Bukkit.broadcastMessage(ChatColor.GREEN + "Everybody is ready!");
				OpenArena.getCurrentArenaGame().start();

			} else if (item.equals(ready)) {
				e.getPlayer().sendMessage(ChatColor.RED + "You are not ready!");
				itemMeta.setDisplayName("Not Ready");
				e.getPlayer().getInventory().getItemInMainHand().setItemMeta(itemMeta);

				e.getPlayer().getInventory().setItemInMainHand(notReady);
				OpenArena.getCurrentArenaGame().getArenaPlayer(e.getPlayer()).notReady();
			} else if (itemMaterial.equals(itemMaterial.CHEST)) {
				ArrayList<Kit> kits = plugin.getActiveKits();

				e.getPlayer().openInventory(plugin.getKitGUI());
			}

			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!OpenArena.getCurrentArenaGame().isActive() && e.getView().getTitle().equalsIgnoreCase("Select a kit")
				&& OpenArena.getCurrentArenaGame().playerInArena((Player) e.getWhoClicked())) {
			Player player = (Player) e.getWhoClicked();
			e.setCancelled(true);
			if (!e.getCurrentItem().hasItemMeta()) {
				return;
			}

			ArrayList<String> kitNames = new ArrayList<String>();
			for (Kit kit : plugin.getActiveKits())
				kitNames.add(kit.getName());

			String kitName = e.getCurrentItem().getItemMeta().getDisplayName();

			if (kitNames.contains(kitName)) {
				player.sendMessage(ChatColor.GREEN + "Your kit: " + kitName);

				ArenaPlayer p = OpenArena.getCurrentArenaGame().getArenaPlayer(player);
				p.setKit(kitName);

				player.setMetadata("kit", new FixedMetadataValue(plugin, kitName));
				player.closeInventory();
			}
			return;
		}
	}

	@EventHandler
	public void takeDamage(EntityDamageEvent e) {
		if (!OpenArena.getCurrentArenaGame().isActive() && e.getEntity() instanceof Player
				&& OpenArena.getCurrentArenaGame().playerInArena((Player) e.getEntity()))
			e.setCancelled(true);
	}

	@EventHandler
	public void dropItem(PlayerDropItemEvent e) {
		if (!OpenArena.getCurrentArenaGame().isActive() && OpenArena.getCurrentArenaGame().playerInArena(e.getPlayer()))
			e.setCancelled(true);
	}
}
