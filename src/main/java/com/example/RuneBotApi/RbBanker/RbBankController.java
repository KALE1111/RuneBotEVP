package com.example.RuneBotApi.RbBanker;

import com.google.inject.Singleton;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;


/**
 * This controller will manage banking for any supported plugin
 * returns true if the calling plugin is meant to yield execution
 */
@Singleton
@PluginDescriptor(
        name = "<html><font color=#86C43F>[RB]</font> RB Banking config</html>",
        description = "Configuration for banking and selling items on the ge",
        tags = {"Banking"}
)
public class RbBankController extends Plugin {

    private State state;

    RbBankController()
    {
        state = State.TELEPORT;
    }

    boolean eventLoop()
    {


        switch (state)
        {
            case TELEPORT:
                break; case OPEN_BANK:
            break; case DEPOSIT_ITEMS:
            break; case WITHDRAW_ITEMS:
            break; case RETURN:
        }

        return true;
    }

    private enum State
    {
        TELEPORT,
        OPEN_BANK,
        DEPOSIT_ITEMS,
        WITHDRAW_ITEMS,
        RETURN
    }
}
