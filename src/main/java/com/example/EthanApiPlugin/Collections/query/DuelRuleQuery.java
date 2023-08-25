package com.example.EthanApiPlugin.Collections.query;

import com.example.EthanApiPlugin.Collections.DuelArenaRules;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;
import net.runelite.client.game.ItemManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DuelRuleQuery {
    private List<Widget> rules;
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    //static ItemManager itemManager = RuneLite.getInjector().getInstance(ItemManager.class);

    public DuelRuleQuery(List<Widget> rules) {
        this.rules = new ArrayList(rules);
    }

    public DuelRuleQuery filter(Predicate<? super Widget> predicate) {
        rules = rules.stream().filter(predicate).collect(Collectors.toList());
        return this;
    }

    public DuelRuleQuery nameContains(String name) {
        rules = rules.stream().filter(item -> item.getName().contains(name)).collect(Collectors.toList());
        return this;
    }
    public List<Widget> result() {
        return rules;
    }

    public Optional<Widget> first() {
        Widget returnWidget = null;
        if (rules.size() == 0) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(rules.get(0));
    }
}
