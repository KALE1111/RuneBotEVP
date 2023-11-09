package com.example.EthanApiPlugin.Collections;

import com.example.EthanApiPlugin.Collections.query.ItemQuery;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;

import java.util.*;
import java.util.stream.Collectors;

public class Inventory {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    static List<Widget> inventoryItems = new ArrayList<>();

    public static ItemQuery search() {
        return new ItemQuery(inventoryItems);
    }

    public static int getEmptySlots() {
        return 28 - search().result().size();
    }
    public static boolean full(){
        return getEmptySlots()==0;
    }

    public static int getItemAmount(int itemId) {
        return search().withId(itemId).result().size();
    }

    public static int getItemAmount(String itemName) {
        return search().withName(itemName).result().size();
    }

    public static void reloadInventory() {
        Inventory.inventoryItems =
                Arrays.stream(client.getWidget(WidgetInfo.INVENTORY).getDynamicChildren()).filter(Objects::nonNull).filter(x -> x.getItemId() != 6512 && x.getItemId() != -1).collect(Collectors.toList());
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged e) {
        client.runScript(6009, 9764864, 28, 1, -1);
        if (e.getContainerId() == 93) {
            reloadInventory();
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() == GameState.HOPPING || gameStateChanged.getGameState() == GameState.LOGIN_SCREEN || gameStateChanged.getGameState() == GameState.CONNECTION_LOST) {
            Inventory.inventoryItems.clear();
        }
    }

    public static boolean useItemNoCase(String name, String... actions) {
        return nameContainsNoCase(name).first().flatMap(item ->
        {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, actions);
            return Optional.of(true);
        }).orElse(false);
    }

    public static ItemQuery nameContainsNoCase(String name) {
        return Inventory.search().filter(widget -> widget.getName().toLowerCase().contains(name.toLowerCase()));
    }

    public static Optional<Widget> getById(int id) {
        return Inventory.search().withId(id).first();
    }

    public static Optional<Widget> getItemNameContains(String name, boolean caseSensitive) {
        if (caseSensitive) {
            return Inventory.search().filter(widget -> widget.getName().contains(name)).first();
        } else {
            return Inventory.search().filter(widget -> widget.getName().toLowerCase().contains(name.toLowerCase())).first();
        }
    }

    public static Optional<Widget> getItemNameContains(String name) {
        return getItemNameContains(name, true);
    }

    public static Optional<Widget> getItem(String name, boolean caseSensitive) {
        if (caseSensitive) {
            return Inventory.search().filter(widget -> widget.getName().equals(name)).first();
        } else {
            return Inventory.search().filter(widget -> widget.getName().toLowerCase().equals(name.toLowerCase())).first();
        }
    }

    public static Optional<Widget> getItem(String name) {
        return getItem(name, true);
    }

    public static int getItemAmount(String name, boolean stacked) {
        if (stacked) {
            return nameContainsNoCase(name).first().isPresent() ? nameContainsNoCase(name).first().get().getItemQuantity() : 0;
        }

        return nameContainsNoCase(name).result().size();
    }

    public static int getItemAmount(int id, boolean stacked) {
        if (stacked) {
            return getById(id).isPresent() ? getById(id).get().getItemQuantity() : 0;
        }

        return Inventory.search().withId(id).result().size();
    }

    public static boolean hasItem(String name) {
        return getItemAmount(name, false) > 0;
    }

    public static boolean hasItem(String name, boolean stacked) {
        return getItemAmount(name, stacked) > 0;
    }

    public static boolean hasItem(String name, int amount) {
        return getItemAmount(name, false) >= amount;
    }

    public static boolean hasItem(String name, int amount, boolean stacked) {
        return getItemAmount(name, stacked) >= amount;
    }

    public static boolean hasItems(String ...names) {
        for (String name : names) {
            if (!hasItem(name)) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasAnyItems(String ...names) {
        for (String name : names) {
            if (hasItem(name)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasItem(int id) {
        return getItemAmount(id) > 0;
    }

    public static List<Widget> getItems() {
        return Inventory.search().result();
    }

    public static int emptySlots() {
        return 28 - Inventory.search().result().size();
    }
}
