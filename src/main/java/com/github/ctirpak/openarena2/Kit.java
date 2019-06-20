package com.github.ctirpak.openarena2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Kit {
	private Material repr;
	private String name, description;
	private HashMap<ItemStack, Integer> startingItems;

	public Kit(Material repr, String name, String description) {
		this.repr = repr;
		this.name = name;
		this.description = description;

		this.startingItems = new HashMap<>();
	}

	// returns a formatted lore for the Kit Selector
	public List<String> getLore() {
		return null;
	}

	public Material getRepr() {
		return repr;
	}

	public void setRepr(Material repr) {
		this.repr = repr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HashMap<ItemStack, Integer> getStartingItems() {
		return startingItems;
	}

	public void setStartingItems(HashMap<ItemStack, Integer> startingItems) {
		this.startingItems = startingItems;
	}

	// loads Kit data from config
	private void loadFromConfig() {

	}

	public void addStartingItem(Material m, int i) {
		HashMap<ItemStack, Integer> hm = new HashMap<ItemStack, Integer>();
		ItemStack item = new ItemStack(m);

		hm.put(item, i);

		this.setStartingItems(hm);
	}

	public List<String> getFormattedLore() {

		List<String> lore = new ArrayList<>();

		lore.add(ChatColor.BOLD + description);
		if (startingItems.isEmpty()) {
			lore.add("No starting Items!");
			return lore;
		}

		lore.add("Start with:");

		Iterator<Entry<ItemStack, Integer>> it = startingItems.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<ItemStack, Integer> startingItem = (Map.Entry<ItemStack, Integer>) it.next();
			lore.add("> " + startingItem.getValue() + "x " + startingItem.getKey().toString());
		}

		return lore;
	}
}
