package com.example.RuneBotApi.RbBanker;


import com.example.EthanApiPlugin.Collections.Inventory;
import com.example.RuneBotApi.Movement;
import com.example.RuneBotApi.Objects.Banks;
import com.example.RuneBotApi.RBApi;
import com.example.RuneBotApi.RBRandom;
import com.example.RuneBotApi.RbExceptions.NoSuchBankItemException;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class ResupplyController {

    private State state = State.DEPOSIT_ITEMS;
    private RbBankConfig config;
    private int timeout = 0;

    public ResupplyController(RbBankConfig config)
    {
        this.config = config;
    }

    public boolean eventLoop()
    {
        if (0 < timeout--) return true;

        switch (state)
        {
            case DEPOSIT_ITEMS:
                Banks.depositInventory();
                state = State.CHECK_EMPTY_INV;
            break; case CHECK_EMPTY_INV:
                if (Inventory.getEmptySlots() < 28) {
                    log.error("Your bank was full when trying to deposit all your items.");
                    RBApi.panic();
                }
//                if (config.sellLoot()) state = State.SELL_ON_GE;
                if (false) state = State.SELL_ON_GE;
                                  else state = State.CHECK_FOR_ITEMS;
            break; case SELL_ON_GE:
            break; case CHECK_FOR_ITEMS:
                Set<String> missingItems = Banks.checkForItems(RBApi.configJSONToHashMap(config.desiredItems()).keySet());
                if (!missingItems.isEmpty()) {
//                    if (config.buyItemsIfNeeded()) {
                    if (false) {
                        state = State.REBUY_MISSING_ITEMS;
                        return true;
                    } else {
                        RBApi.panic();
                        throw new NoSuchBankItemException("The following item(s) were missing from your bank: " + missingItems + ".");
                    }
                }
                state = State.WITHDRAW_ITEMS;
            break; case REBUY_MISSING_ITEMS:
                System.out.println();
            break; case WITHDRAW_ITEMS:
                if (!Banks.withdrawItems(RBApi.configJSONToHashMap(config.desiredItems()))) state = State.CLOSE_BANK;
            break; case CLOSE_BANK:
                Movement.moveRelative(RBRandom.randRange(-10, -1), RBRandom.randRange(-10, 0));
                return false;

        }

        return true;
    }

    private enum State
    {
        DEPOSIT_ITEMS,
        CHECK_EMPTY_INV,
        SELL_ON_GE,
        CHECK_FOR_ITEMS,
        WITHDRAW_ITEMS,
        REBUY_MISSING_ITEMS,
        CLOSE_BANK
    }
}
