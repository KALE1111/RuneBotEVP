package com.example.RuneBotApi.Objects;

import com.example.EthanApiPlugin.Collections.TileObjects;
import com.example.InteractionApi.TileObjectInteraction;
import com.example.RuneBotApi.RBApi;
import com.example.RuneBotApi.RbBanker.RbBankConfig;
import com.example.RuneBotApi.RbExceptions.AwaitTimeoutException;
import com.example.RuneBotApi.RbExceptions.InvalidConfigException;
import com.example.RuneBotApi.RbExceptions.NoSuchGameObjectException;
import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;
import net.runelite.client.config.ConfigManager;

import java.util.Optional;

/**
 * Opens the nearest banking object (some NPCs cause you to get stuck)
 * returns true if you should yield execution to this class
 */
public class Banks {
    private static final Client client = RuneLite.getInjector().getInstance(Client.class);

    private static int errTimeout = 0;
    private static int bankPinIndex = 0;
    private static boolean attemptingPin = false;
    private static final RbBankConfig config = RBApi.getConfigManager().getConfig(RbBankConfig.class);


    public static boolean openNearestBank() throws AwaitTimeoutException
    {
        Optional<TileObject> bank = TileObjects.search().withAction("Bank").nearestToPlayer();
        if (bank.isEmpty()) {
            bank = TileObjects.search().withAction("Grand Exchange booth").nearestToPlayer();
        }

        // if bank isn't open, open it
        if (client.getWidget(WidgetInfo.BANK_CONTAINER) == null && client.getWidget(WidgetInfo.BANK_PIN_CONTAINER) == null) {
            // if neither the bank nor pin widgets are open and this flag is set, we entered the pin incorrectly
            if (attemptingPin) {
                attemptingPin = false;
                throw new InvalidConfigException("Invalid bank pin provided in Banker/Seller config.");
            }

            if (errTimeout++ == 0) {
                if (bank.isPresent()) {
                    TileObjectInteraction.interact(bank.get(), "Bank");
                    return true;
                } else {
                    errTimeout = 0;
                    throw new NoSuchGameObjectException("No valid banking object was found in this location. Maybe you're looking for a banking npc?");
                }
            } else {
                if (++errTimeout > 50) {
                    errTimeout = 0;
                    throw new AwaitTimeoutException("50 game ticks have passed since clicking a banking object in Banks.openNearestBank().");
                }
                return true;
            }
        } else {
            // if we don't have the bank open, try to enter the bank pin and set a flag.
            if (client.getWidget(WidgetInfo.BANK_PIN_CONTAINER) == null) {
                errTimeout = 0;
                return false; // if the bank is open, return execution to caller
            }
            attemptingPin = true;
            errTimeout = 0;
            try {
                return enterBankPin(config.bankPin());
            } catch (Exception e) {
                RBApi.panic();
            }
        }

        return false;
    }

    public static boolean enterBankPin(String pin)
    {

        char[] pinArray = pin.toCharArray();

        if (pinArray.length != 4) throw new InvalidConfigException("Bank pin must be exactly 4 characters.");
        if (pinArray[bankPinIndex] < '0' || pinArray[bankPinIndex] > '9')
            throw new InvalidConfigException("Bank pin must only contain numbers.. baka");

        RBApi.sendKeystroke(pinArray[bankPinIndex++]);

        if (bankPinIndex > 3) {
            bankPinIndex = 0;
            return false;
        }

        return true;
    }
}
