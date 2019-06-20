package com.github.ctirpak.openarena2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ctirpak.openarena2.generators.ChestGenerator;
import com.github.ctirpak.openarena2.listeners.LobbyEventListener;

public class OpenArena extends JavaPlugin {
	private static ArenaGame ag;
	private ChestGenerator cg;
	private Logger logger;
	public static Inventory kitSelector;
	public static Inventory lobbyMenu;
	private static HashMap<String, Kit> kits;
	private Inventory kitGUI;

	@Override
	public void onEnable() {
		this.loadKits();
		this.getCommand("oa").setExecutor(new OACommandExecutor(this));
		new LobbyEventListener(this);

		logger = new Logger(ChatColor.YELLOW, "OpenArena > ");
		ag = new ArenaGame(this);

		OpenArena.lobbyMenu = generateLobbyMenu();
		getLogger().info("OpenArena has been enabled");
	}

	@Override
	public void onDisable() {
		ag.stop();
		getLogger().info("OpenArena has been disabled");
	}

	// loads all kits that are marked as active from the config file
	private void loadKits() {
		Kit archer = new Kit(Material.BOW, "Archer", "Get an early advantage over your foes with a set of a bow and arrows.");
		Kit lumberjack = new Kit(Material.IRON_AXE, "Lumberjack", "Waste less time gathering wood.");
		Kit cultivist = new Kit(Material.WHEAT, "Cultivist", "Easily provide food for yourself and your team.");
		Kit demolitionist =new Kit(Material.TNT, "Demolitionist", "Wreak havoc with explosives.");
		Kit knight = new Kit(Material.SADDLE, "Knight", "Ride horses!");
		Kit alchemist = new Kit(Material.BREWING_STAND, "Alchemist", "Use your brewing stand to create powerful potions.");
		Kit enchanter = new Kit(Material.ENCHANTING_TABLE, "Enchanter", "Enchant your items.");
		Kit spy = new Kit(Material.COMPASS, "Spy", "Your compass gives you more information about your foes.");
		Kit berserker = new Kit(Material.POTION, "Berserker", "Killing mobs and players gives you extra strength.");

		archer.addStartingItem(Material.BOW, 1);
		archer.addStartingItem(Material.ARROW, 24);
		lumberjack.addStartingItem(Material.IRON_AXE, 1);
		demolitionist.addStartingItem(Material.TNT, 16);
		demolitionist.addStartingItem(Material.FLINT_AND_STEEL, 1);
		knight.addStartingItem(Material.SADDLE, 1);
		alchemist.addStartingItem(Material.BREWING_STAND, 1);
		enchanter.addStartingItem(Material.ENCHANTING_TABLE, 1);
		
		OpenArena.kits = new HashMap<String, Kit>();
		OpenArena.kits.put(archer.getName(),archer);
		OpenArena.kits.put(lumberjack.getName(),lumberjack);
		OpenArena.kits.put(cultivist.getName(),cultivist);
		OpenArena.kits.put(demolitionist.getName(),demolitionist);
		OpenArena.kits.put(knight.getName(),knight);
		OpenArena.kits.put(alchemist.getName(),alchemist);
		OpenArena.kits.put(enchanter.getName(),enchanter);
		OpenArena.kits.put(spy.getName(),spy);
		OpenArena.kits.put(berserker.getName(),berserker);

		Inventory kg = Bukkit.createInventory(null, 9, "Select a kit");
		
		for (Map.Entry<String, Kit> entry : kits.entrySet()) {
			Kit kit = entry.getValue();
			ItemStack kitItem = new ItemStack(kit.getRepr(), 1);
			ItemMeta kitItemMeta = kitItem.getItemMeta();

			kitItemMeta.setDisplayName(kit.getName());
			
			List<String> lore = kit.getFormattedLore();
			lore.add(kit.getDescription());

			kitItemMeta.setLore(lore);

			kitItem.setItemMeta(kitItemMeta);
			kg.addItem(kitItem);
		}
		this.setKitGUI(kg);
	}

	// generates the Inventory GUI for the players in the lobby
	private Inventory generateLobbyMenu() {
		Inventory inv = Bukkit.getServer().createInventory(null, InventoryType.PLAYER);

		inv.setItem(0, getKitSelector());
		inv.setItem(8, getReadyItems()[0]);

		return inv;
	}

	public ItemStack getKitSelector() {
		ItemStack kitSelector = new ItemStack(Material.CHEST, 1);

		ItemMeta kitSelectorMeta = kitSelector.getItemMeta();
		kitSelectorMeta.setDisplayName("Kit Selector");
		kitSelector.setItemMeta(kitSelectorMeta);
		return kitSelector;
	}

	// this returns an array of the two items that the user can toggle to be ready
	// or not.
	public ItemStack[] getReadyItems() {
		ItemStack[] readyItems = new ItemStack[2];

		ItemStack notReady = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1); //red
		ItemMeta notReadyMeta = notReady.getItemMeta();
		notReadyMeta.setDisplayName("Not Ready");
		notReady.setItemMeta(notReadyMeta);

		ItemStack ready = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1); //green
		ItemMeta readyMeta = ready.getItemMeta();
		readyMeta.setDisplayName("Ready");
		ready.setItemMeta(readyMeta);

		readyItems[0] = notReady;
		readyItems[1] = ready;

		return readyItems;
	}

	// returns the arena game that is currently active/in lobby mode.
	public static ArenaGame getCurrentArenaGame() {
		return ag;
	}

	public void log(String msg) {
		logger.log(msg);
	}

	public ArrayList<Kit> getActiveKits() {
		ArrayList<Kit> kits = new ArrayList<Kit>();
		Iterator<Entry<String, Kit>> iter = OpenArena.kits.entrySet().iterator();
		
		while(iter.hasNext()) {
			kits.add(iter.next().getValue()); 
		}
		return kits;
	}
	public static Kit getKit(String kitName) {
		Kit k = OpenArena.kits.get(kitName);
		return k;
		
	}

	public Inventory getKitGUI() {
		return kitGUI;
	}

	public void setKitGUI(Inventory kitGUI) {
		this.kitGUI = kitGUI;
	}
}
