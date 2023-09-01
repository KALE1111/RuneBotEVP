package com.example.RuneBotApi.RbBanker;

import com.example.RuneBotApi.RBApi;
import net.runelite.api.Client;

public class BankingHandler {

    private final Client client = RBApi.getClient();
    private String depositConfig;

    public boolean depositItems(String depoConfig)
    {
        depositConfig = depoConfig;
        System.out.println("BankingHandler.depositItems");

        return true;
    }
}
