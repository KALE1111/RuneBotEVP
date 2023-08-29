package com.example.RuneBotApi.RbBanker;

import com.example.RuneBotApi.MapSquare;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("BankConfig")
public interface RbBankConfig extends Config {

    @ConfigItem(
            keyName = "bankingLocation",
            name = "Banking Location",
            description = "Which POH portal are you going to use?",
            position = 1
    )
    default BankingLocation bankingLocation() { return BankingLocation.GRAND_EXCHANGE; }


    @ConfigItem(
            keyName = "desiredInventory",
            name = "Inventory Items",
            description = "shark: 10, super restore: 4, etc: 3",
            position = 3
    )
    default String desiredItems () { return ""; }

    @ConfigItem(
            keyName = "usedAmmo",
            name = "Ammunition",
            description = "adamant dart, trident of the swamp, crumble undead, etc",
            position = 4
    )
    default String usedAmmo () { return ""; }

    @ConfigItem(
            keyName = "loginName",
            name = "Login Name (optional)",
            description = "Username used to login to the account",
            position = 5
    )
    default String loginName() { return ""; }

    @ConfigItem(
            keyName = "loginPassword",
            name = "Login Password (optional)",
            description = "Password used to login to the account",
            position = 6
    )
    default String loginPassword() { return ""; }

    @AllArgsConstructor
    @Getter
    enum BankingLocation
    {
        GRAND_EXCHANGE(new ObjectActionLocationTrio("_GE or Varrock portal", "Grand Exchange", MapSquare.GRAND_EXCHANGE.getId()));

        private final ObjectActionLocationTrio objectActionPair;
    }

    class ObjectActionLocationTrio {

        private final String left;
        private final String right;
        private final int locationId;

        ObjectActionLocationTrio(String left, String right, int locationId)
        {
            this.left = left;
            this.right = right;
            this.locationId = locationId;
        }

        public String getObject() {
            return left;
        }

        public String getAction() {
            return right;
        }

        public int getLocationId() {
            return locationId;
        }
    }
}
