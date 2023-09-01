package com.example.RuneBotApi.RbBanker;

import com.example.RuneBotApi.MapSquare;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("BankConfig")
public interface RbBankConfig extends Config {
//
//    @ConfigItem(
//            keyName = "bankingLocation",
//            name = "Banking Location",
//            description = "Which POH portal are you going to use?",
//            position = 1
//    )
//    default BankingLocation bankingLocation() { return BankingLocation.GRAND_EXCHANGE; }
//
//
//    @ConfigItem(
//            keyName = "desiredInventory",
//            name = "Inventory Items",
//            description = "void mage helm: 1, shark: 10, super rest*: 2, Teleport to house: >3, 100",
//            position = 3
//    )
//    default String desiredItems() { return ""; }
//
//    @ConfigItem(
//            keyName = "usedAmmo",
//            name = "Ammunition",
//            description = "adamant dart, trident of the swamp, crumble undead, etc",
//            position = 4
//    )
//    default String usedAmmo() { return ""; }
//
//    @ConfigItem(
//            keyName = "buySupplies",
//            name = "Rebuy Supplies?",
//            description = "Buys supplies from the GE if you run out",
//            position = 5
//    )
//    default boolean buyItemsIfNeeded() { return false; }
//
//    @ConfigItem(
//            keyName = "sellLoot",
//            name = "Sell loot?",
//            description = "Sells loot on the GE before 6 hour log",
//            position = 6
//    )
//    default boolean sellLoot() { return false; }
//
//    @ConfigItem(
//            keyName = "lootList",
//            name = "Loot List",
//            description = "item 1, item 2, ite*, item 4: amount to leave in bank, item 5: 100",
//            position = 7
//    )
//    default String lootList() { return ""; }
//
//    @ConfigItem(
//            keyName = "loginName",
//            name = "Login Name (optional)",
//            description = "Username used to login to the account",
//            position = 8
//    )
//    default String loginName() { return ""; }
//
//    @ConfigItem(
//            keyName = "loginPassword",
//            name = "Login Password (optional)",
//            description = "Password used to login to the account",
//            position = 9
//    )
//    default String loginPassword() { return ""; }

    @ConfigItem(
            keyName = "bankPin",
            name = "Bank Pin",
            description = "Your bank pin",
            position = 10
    )
    default String bankPin() { return ""; }

    @AllArgsConstructor
    @Getter
    enum BankingLocation
    {
        GRAND_EXCHANGE(new BankingData("_GE or Varrock portal", "Grand Exchange", MapSquare.GRAND_EXCHANGE.getId()));

        private final BankingData bankingData;
    }

    @Getter
    @AllArgsConstructor
    class BankingData
    {
        private final String teleportObject;
        private final String action;
        private final int locationId;
    }
}
