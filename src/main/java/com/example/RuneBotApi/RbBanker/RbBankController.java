package com.example.RuneBotApi.RbBanker;

import com.example.EthanApiPlugin.Collections.Inventory;
import com.example.EthanApiPlugin.Collections.TileObjects;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.InteractionApi.InventoryInteraction;
import com.example.InteractionApi.TileObjectInteraction;
import com.example.RuneBotApi.LocalPlayer.LocationInformation;
import com.example.RuneBotApi.RBApi;
import com.example.RuneBotApi.RBConstants;
import com.example.RuneBotApi.RbExceptions.NoSuchGameObjectException;
import com.example.RuneBotApi.RbExceptions.NoWalkablePathException;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.Optional;


/**
 * This controller will manage banking for any supported plugin
 * returns true if the calling plugin is meant to yield execution
 */
@Singleton
@PluginDescriptor(
        name = "<html><font color=#86C43F>[RB]</font> Banker/Seller</html>",
        description = "Configuration for banking and selling items on the ge",
        enabledByDefault = true,
        tags = {"Banking"},
        hidden = false
)
public class RbBankController extends Plugin {

    private int timeout = 0;
    private State state;
    private RbBankConfig config = RBApi.getConfigManager().getConfig(RbBankConfig.class);
    @Provides
    public RbBankConfig getConfig(ConfigManager configManager) {
        return config;
    }
    private Client client = RBApi.getClient();


    private boolean sellItems = false;

    //var for if we want to sell items at [config] hours


    public RbBankController()
    {
//        state = State.TELEPORT;
        state = State.USE_POOL;
    }

    public boolean eventLoop() {
        if (0 < timeout--) return true; // only exec if no timeout


        switch (state)
        {
            case TELEPORT:
                if (Inventory.search().withId(RBConstants.houseTabId).result().isEmpty()) state = State.FAILURE;
                InventoryInteraction.useItem(RBConstants.houseTabId, "Break");
                timeout = 8;
                state = State.DOOR_STUCK;
            break; case USE_POOL:
                Optional<TileObject> pool = TileObjects.search().withAction("Drink").first();
                System.out.println("pool.isPresent() = " + pool.isPresent());
                if (pool.isPresent()) {
                    TileObjectInteraction.interact(pool.get(), "Drink");
                    state = State.AWAIT_POOL;
                    timeout = 2;
                    return true;
                } else {
                    state = State.DOOR_STUCK;
                }
            break; case AWAIT_POOL:
                if (!EthanApiPlugin.isMoving()) state = State.DOOR_STUCK;
            break; case DOOR_STUCK:
                Optional<TileObject> bankingObject = getBankingObject();

                if (bankingObject.isPresent())
                {
                    TileObject obj = bankingObject.get();

                    // we need to have LoS to at least 2 orthogonal tiles otherwise we could end up on the other side of a wall
                    // finding pathable tiles to object.getWorldLocation doesn't work since the destination resides within the obj
                    int pathable = 0;
                    pathable += EthanApiPlugin.canPathToTile(obj.getWorldLocation().dx(1)).isReachable() ? 1 : 0;
                    pathable += EthanApiPlugin.canPathToTile(obj.getWorldLocation().dx(-1)).isReachable() ? 1 : 0;
                    pathable += EthanApiPlugin.canPathToTile(obj.getWorldLocation().dy(1)).isReachable() ? 1 : 0;
                    pathable += EthanApiPlugin.canPathToTile(obj.getWorldLocation().dy(-1)).isReachable() ? 1 : 0;

                    if (pathable > 1) {
                        TileObjectInteraction.interact(bankingObject.get(), config.bankingLocation().getObjectActionLocationTrio().getAction());
                        state = State.EXIT_POH;
                    } else {
                        state = State.FAILURE;
                        throw new NoWalkablePathException("Cannot path to object '" + config.bankingLocation().getObjectActionLocationTrio().getObject() + "'");
                    }

                } else {
                    state = State.FAILURE;
                    throw new NoSuchGameObjectException("Object '" + config.bankingLocation().getObjectActionLocationTrio().getObject() + "' does not exist in the current scene'");
                }
            break; case EXIT_POH:
                if (LocationInformation.getMapSquareId() != config.bankingLocation().getObjectActionLocationTrio().getLocationId())
                    return true;
                state = State.OPEN_BANK;
            break; case OPEN_BANK:
            break; case FAILURE:
        }
        return true;
    }


    private Optional<TileObject> getBankingObject()
    {
        Optional<TileObject> bankingObject;
        if (config.bankingLocation() == RbBankConfig.BankingLocation.GRAND_EXCHANGE) {
            bankingObject = TileObjects.search().withId(13615).first();
        } else {
            bankingObject = TileObjects.search().withName(config.bankingLocation().getObjectActionLocationTrio().getObject()).first();
        }
        return bankingObject;
    }

    private enum State
    {
        TELEPORT,
        USE_POOL,
        AWAIT_POOL,
        DOOR_STUCK,
        EXIT_POH,
        OPEN_BANK,
        DEPOSIT_ITEMS,
        // no sell item check in case bad rng and account dies over and over and over and we don't hit this point before 6h log
        // always check to see if we will use the GeSeller class from the class that calls this event loop
        WITHDRAW_ITEMS,
        RETURN,
        FAILURE // implement failure handler that logs user out and prints the reason in the log file
    }

}
