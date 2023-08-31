package com.example.RuneBotApi.Objects;

import com.example.EthanApiPlugin.Collections.TileObjects;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.InteractionApi.TileObjectInteraction;
import com.example.RuneBotApi.RbExceptions.AwaitTimeoutException;
import com.example.RuneBotApi.RbExceptions.NoSuchGameObjectException;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;

import java.util.Optional;

/**
 * Opens the nearest banking object (some NPCs cause you to get stuck)
 * returns true if you should yield execution to this class
 */
public class Banks {
    static Client client = RuneLite.getInjector().getInstance(Client.class);

    private static int errTimeout = 0;

    public static boolean openNearestBank()
    {

        Optional<TileObject> bank = TileObjects.search().withAction("Bank").nearestToPlayer();
        if (bank.isEmpty()) {
            bank = TileObjects.search().withAction("Grand Exchange booth").nearestToPlayer();
        }

        // if bank isn't open, open it
        if (client.getWidget(WidgetInfo.BANK_CONTAINER) == null) {
            if (errTimeout == 0) {
                if (bank.isPresent()) {
                    TileObjectInteraction.interact(bank.get(), "Bank");
                    return true;
                } else {
                    throw new NoSuchGameObjectException("No valid banking object was found in this location. Maybe you're looking for a banking npc?");
                }
            } else {
                if (++errTimeout >= 50) {
                    throw new AwaitTimeoutException("50 game ticks have passed since clicking a banking object in Banks.openNearestBank().");
                }
                return true;
            }
        } else {
            errTimeout = 0;
        }

        return false;
    }
}
