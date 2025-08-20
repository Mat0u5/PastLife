package net.mat0u5.pastlife.utils;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryStorage {
    private static final Map<String, PlayerInventoryData> storedInventories = new HashMap<>();

    public static class PlayerInventoryData {
        public final ItemStack[] items;
        public final ItemStack[] armor;

        public PlayerInventoryData(ItemStack[] items, ItemStack[] armor) {
            this.items = items;
            this.armor = armor;
        }
    }

    public static void storeInventory(String playerName, PlayerInventory inventory) {
        ItemStack[] storedItems = new ItemStack[inventory.inventorySlots.length];
        ItemStack[] storedArmor = new ItemStack[inventory.armorSlots.length];

        for (int i = 0; i < inventory.inventorySlots.length; i++) {
            if (inventory.inventorySlots[i] != null) {
                storedItems[i] = inventory.inventorySlots[i].copy();
            }
        }

        for (int i = 0; i < inventory.armorSlots.length; i++) {
            if (inventory.armorSlots[i] != null) {
                storedArmor[i] = inventory.armorSlots[i].copy();
            }
        }

        storedInventories.put(playerName, new PlayerInventoryData(storedItems, storedArmor));
    }

    public static void restoreInventory(String playerName, PlayerInventory inventory) {
        PlayerInventoryData data = storedInventories.remove(playerName);
        if (data != null) {
            for (int i = 0; i < data.items.length && i < inventory.inventorySlots.length; i++) {
                inventory.inventorySlots[i] = data.items[i];
            }

            for (int i = 0; i < data.armor.length && i < inventory.armorSlots.length; i++) {
                inventory.armorSlots[i] = data.armor[i];
            }
        }
    }

    public static boolean hasStoredInventory(String playerName) {
        return storedInventories.containsKey(playerName);
    }
}
