package com.example.RuneBotApi.Objects;

import com.example.EthanApiPlugin.Collections.Bank;
import com.example.EthanApiPlugin.Collections.Inventory;
import com.example.EthanApiPlugin.Collections.TileObjects;
import com.example.EthanApiPlugin.Collections.query.ItemQuery;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.InteractionApi.BankInteraction;
import com.example.InteractionApi.TileObjectInteraction;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import com.example.RuneBotApi.RBApi;
import com.example.RuneBotApi.RBRandom;
import com.example.RuneBotApi.RbBanker.RbBankConfig;
import com.example.RuneBotApi.RbExceptions.AwaitTimeoutException;
import com.example.RuneBotApi.RbExceptions.InvalidConfigException;
import com.example.RuneBotApi.RbExceptions.NoSuchGameObjectException;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;

import java.util.*;

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
    private static int configIndex = 0;
    private static int stopAt = 0;


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
                RBApi.panic();
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

        if (pinArray.length != 4) {
            attemptingPin = false;
            throw new InvalidConfigException("Bank pin must be exactly 4 characters.");
        }
        if (pinArray[bankPinIndex] < '0' || pinArray[bankPinIndex] > '9') {
            attemptingPin = false;
            throw new InvalidConfigException("Bank pin must only contain numbers.. baka");
        }

        RBApi.sendKeystroke(pinArray[bankPinIndex++]);

        if (bankPinIndex > 3) {
            bankPinIndex = 0;
            return false;
        }

        return true;
    }

    public static boolean depositInventory()
    {
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, 786474, -1, -1);
        return true;
    }

    /**
     * currently all options other than "Withdraw-X" are supported
     * will perform between 3 and 6 clicks per tick
     */
    public static boolean withdrawItems(Map<String, Integer> configItems)
    {
        return withdrawItemsWithAmount(configItems, RBRandom.randRange(3, 6));
    }

    /**
     * currently all options other than "Withdraw-X" are supported
     * will perform clickLimiter clicks per tick
     */
    public static boolean withdrawItems(Map<String, Integer> configItems, int clickLimiter)
    {
        return withdrawItemsWithAmount(configItems, clickLimiter);
    }

    public static boolean withdrawItems(Map<String, Integer> configItems, int clickLimiter, boolean sensy)
    {
        if(sensy){
            return withdrawItemsWithAmount(configItems, clickLimiter,true);
        }
        else {
            return withdrawItemsWithAmount(configItems, clickLimiter);
        }
    }

    private static boolean withdrawItemsWithAmount(Map<String, Integer> configItems, int clickLimiter, boolean Sensitive) {
        int index = 0;
        int clicks = 0;
        stopAt = clickLimiter;
        Set<String> keys = configItems.keySet();
        Iterator var7 = keys.iterator();

        while(var7.hasNext()) {
            String key = (String)var7.next();
            if (index++ >= configIndex) {
                ++configIndex;
                Widget item;
                if(Sensitive) {
                     item = (Widget) Bank.search().withName(key).first().orElseThrow();
                }
                else{
                     item = (Widget) Bank.search().nameContainsInsensitive(key).first().orElseThrow();
                }
                int itemAmount = (Integer)configItems.get(key);
                clicks += withdrawAmount(item, itemAmount);
                if (clicks == -1) {
                    configIndex = 0;
                    return false;
                }

                if (clicks >= stopAt) {
                    return true;
                }
            }
        }

        configIndex = 0;
        return false;
    }

    private static boolean withdrawItemsWithAmount(Map<String, Integer> configItems, int clickLimiter)
    {
        int index = 0;
        int itemAmount;
        int clicks = 0;
        stopAt = clickLimiter;
        Widget item;

        Set<String> keys = configItems.keySet();

        for (String key : keys)
        {
            if (index++ < configIndex) continue;
            configIndex++;
            item = Bank.search().nameContainsInsensitive(key).first().orElseThrow();
            itemAmount = configItems.get(key);

            clicks += withdrawAmount(item, itemAmount);

            if (clicks == -1) {
                configIndex = 0;
                return false;
            }

            if (clicks >= stopAt) return true;
        }

        configIndex = 0;
        return false;
    }

    private static int withdrawAmount(Widget item, int amount)
    {
        if (amount > 28 || amount == -1) { BankInteraction.useItem(item, "Withdraw-All"); return 1; }
        if (amount == -2) { BankInteraction.useItem(item, "Withdraw-All-but-1"); return 1;  }

        int clicks = 0;

        while (amount >= 10) {
            BankInteraction.useItem(item, "Withdraw-10");
            amount -= 10;
            clicks += 1;
        }
        if (Inventory.full()) return -1;

        while (amount >= 5) {
            BankInteraction.useItem(item, "Withdraw-5");
            amount -= 5;
            clicks += 1;
        }
        if (Inventory.full()) return -1;

        while (amount >= 1) {
            BankInteraction.useItem(item, "Withdraw-1");
            amount -= 1;
            clicks += 1;
        }

        return clicks;
    }

    public static Set<String> checkForItems(Set<String> keys, boolean sensitive)
    {
        Set<String> missingItems = new HashSet<>();

        ItemQuery query;

        for (String k : keys) {
            if(!sensitive) {
                query = Bank.search().nameContainsInsensitive(k);
            }
            else{
                query = Bank.search().withName(k);
            }

            if (query.result().size() > 1) {
                RBApi.panic();
                throw new InvalidConfigException("There were multiple items found in the bank for config item: " + k);
            }

            if (query.result().size() == 0) {
                missingItems.add(k);
            }
        }

        return missingItems;
    }

    public static Set<String> checkForItems(Set<String> keys)
    {
        Set<String> missingItems = new HashSet<>();

        ItemQuery query;

        for (String k : keys) {
            query = Bank.search().nameContainsInsensitive(k);

            if (query.result().size() > 1) {
                //RBApi.panic();
                EthanApiPlugin.sendClientMessage("There were multiple items found in the bank for config item: " + k);
                throw new InvalidConfigException("There were multiple items found in the bank for config item: " + k);
            }

            if (query.result().size() == 0) {
                missingItems.add(k);
            }
        }

        return missingItems;
    }

}
