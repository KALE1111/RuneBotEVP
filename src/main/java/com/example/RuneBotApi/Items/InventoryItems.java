package com.example.RuneBotApi.Items;

import com.example.InteractionApi.InventoryInteraction;
import com.example.RuneBotApi.RBConstants;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;

import java.util.HashSet;
import java.util.Set;

import static com.example.PacketUtils.PacketReflection.client;


public class InventoryItems {
    private static final ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);

    /**
     * for clearing up inventory space for looting. Will drop brews with the lowest priority at the expense
     * of dropping the most valuable food with the highest priority.
     * Returns true if we could drop an item
     */
    public static boolean makeInvSpace(int amount)
    {
        int itemToDrop = -1;
        Set<Integer> inventoryIds = new HashSet<>();
        for (int i = 0; i < 29; ++i) {
            Item item = container.getItem(i);
            if (item != null) inventoryIds.add(item.getId());
        }

        if (inventoryIds.contains(ItemID.VIAL)) itemToDrop = ItemID.VIAL;

        if (itemToDrop == -1) {
            for (int food : RBConstants.foodIds) {
                if (inventoryIds.contains(food)) {
                    itemToDrop = food;
                }
            }
        }

        if (itemToDrop != -1) {
            for (int i = 0; i < amount; ++i)
                InventoryInteraction.useItem(itemToDrop, "Drop");
            return true;
        }

        return false;
    }
}
