package com.example.RuneBotApi.RbBanker;

import com.example.EthanApiPlugin.Collections.Inventory;
import com.example.EthanApiPlugin.Collections.TileObjects;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.InteractionApi.InventoryInteraction;
import com.example.InteractionApi.TileObjectInteraction;
import com.example.RuneBotApi.LocalPlayer.LocationInformation;
import com.example.RuneBotApi.Objects.Banks;
import com.example.RuneBotApi.RBApi;
import com.example.RuneBotApi.RBConstants;
import com.example.RuneBotApi.RbExceptions.NoSuchGameObjectException;
import com.example.RuneBotApi.RbExceptions.NoSuchInventoryItemException;
import com.example.RuneBotApi.RbExceptions.NoWalkablePathException;
import com.google.inject.Provides;
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
//@Singleton
@PluginDescriptor(
        name = "<html><font color=#86C43F>[RB]</font> Banker/Seller</html>",
        description = "Configuration for banking and selling items on the ge",
        tags = {"Banking"}
)
public class RbBankController extends Plugin {
//
//    protected int timeout = 0;
//    protected int errTimer = 0;
//    private State state;
    protected RbBankConfig config = RBApi.getConfigManager().getConfig(RbBankConfig.class);
    @Provides
    public RbBankConfig getConfig(ConfigManager configManager) {
        return config;
    }
//    protected Client client = RBApi.getClient();
//    private ResupplyController resupplyController;
//
//
//    protected boolean sellItems = false;
//    protected boolean buySuppliesIfNeeded = true;
//
//    //var for if we want to sell items at [config] hours
//
//
//    public RbBankController()
//    {
////        this.resupplyController = new ResupplyController();
//        state = State.OPEN_BANK;
//    }
//
//    public boolean eventLoop() {
//        if (0 < timeout--) return true; // only exec if no timeout
//
//
//        switch (state)
//        {
//            case TELEPORT:
//                if (Inventory.search().withId(RBConstants.houseTabId).result().isEmpty())
//                    throw new NoSuchInventoryItemException("No house tabs found in inventory.");
//                InventoryInteraction.useItem(RBConstants.houseTabId, "Break");
//                timeout = 8;
//                state = State.DOOR_STUCK;
//            break; case USE_POOL:
//                Optional<TileObject> pool = TileObjects.search().withAction("Drink").first();
//                if (pool.isPresent()) {
//                    TileObjectInteraction.interact(pool.get(), "Drink");
//                    state = State.AWAIT_POOL;
//                    return true;
//                } else {
//                    state = State.DOOR_STUCK;
//                }
//            break; case AWAIT_POOL:
//                if (!EthanApiPlugin.isMoving()) state = State.DOOR_STUCK;
//            break; case DOOR_STUCK:
//                Optional<TileObject> teleportObject = getTeleportObject();
//                if (teleportObject.isPresent()) {
//                    state = exitIfPathable(teleportObject.get()); // EXIT_POH on success
//                } else {
//                    state = State.FAILURE;
//                    throw new NoSuchGameObjectException("Object '" + config.bankingLocation().getBankingData().getTeleportObject() + "' does not exist in the current scene.");
//                }
//            break; case EXIT_POH:
//                if (LocationInformation.getMapSquareId() != config.bankingLocation().getBankingData().getLocationId()) {
//                    if (errTimer++ > 20) {
//                        state = State.DOOR_STUCK;
//                        errTimer = 0;
//                    }
//                    return true;
//                }
//                state = State.OPEN_BANK;
//            break; case OPEN_BANK:
//                try {
//                    if (!Banks.openNearestBank()) state = State.BANK_MANAGER;
//                } catch (Exception e) {
//                    RBApi.panic();
//                }
//            break; case BANK_MANAGER:
//                return resupplyController.eventLoop(); // when this returns false we return execution to the yielding class
//
//        }
//        return true;
//    }
//
//    private State exitIfPathable(TileObject obj)
//    {
//        // we need to have LoS to at least 2 orthogonal tiles otherwise we could end up on the other side of a wall
//        // finding pathable tiles to object.getWorldLocation doesn't work since the destination resides within the obj
//        int pathable = 0;
//        pathable += EthanApiPlugin.canPathToTile(obj.getWorldLocation().dx(1)).isReachable() ? 1 : 0;
//        pathable += EthanApiPlugin.canPathToTile(obj.getWorldLocation().dx(-1)).isReachable() ? 1 : 0;
//        pathable += EthanApiPlugin.canPathToTile(obj.getWorldLocation().dy(1)).isReachable() ? 1 : 0;
//        pathable += EthanApiPlugin.canPathToTile(obj.getWorldLocation().dy(-1)).isReachable() ? 1 : 0;
//
//        if (pathable > 1) {
//            TileObjectInteraction.interact(obj, config.bankingLocation().getBankingData().getAction());
//            return State.EXIT_POH;
//        } else {
//            state = State.FAILURE;
//            throw new NoWalkablePathException("Cannot path to object '" + config.bankingLocation().getBankingData().getTeleportObject() + "'.");
//        }
//    }
//
//    private Optional<TileObject> getTeleportObject()
//    {
//        Optional<TileObject> bankingObject;
//        if (config.bankingLocation() == RbBankConfig.BankingLocation.GRAND_EXCHANGE) {
//            bankingObject = TileObjects.search().withId(13615).first();
//        } else {
//            bankingObject = TileObjects.search().withName(config.bankingLocation().getBankingData().getTeleportObject()).first();
//        }
//        return bankingObject;
//    }
//
//    private enum State
//    {
//        TELEPORT,
//        USE_POOL,
//        AWAIT_POOL,
//        DOOR_STUCK,
//        EXIT_POH,
//        OPEN_BANK,
//        BANK_MANAGER,
//        FAILURE // implement failure handler that logs user out and prints the reason in the log file
//    }

}
