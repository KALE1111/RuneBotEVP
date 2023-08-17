package com.example.EthanApiPlugin.Collections;

import com.example.EthanApiPlugin.Collections.query.DuelRuleQuery;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;

import java.util.*;
import java.util.stream.Collectors;

public class DuelArenaRules
{
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    static List<Widget> duelRules = new ArrayList<>();

    public static DuelRuleQuery search() {
        createCollection();
        return new DuelRuleQuery(duelRules);
    }

    public static void createCollection(){
        ArrayList<Widget> rule = new ArrayList<>();
        for (int i = 30; i <= 41 ; i++) {
            rule.add(client.getWidget(755,i));
        }
        
        DuelArenaRules.duelRules = rule;
    }
    public static List<Boolean>getRules(){
        createCollection();
        List<Boolean> ruleResults = new ArrayList<>();
        for(Widget child : DuelArenaRules.duelRules){
            if(child.getChildren()[1].getSpriteId() == 699){
                ruleResults.add(true);
            }
            else{
                ruleResults.add(false);
            }
        }
        return ruleResults;
    }

    public static HashMap<Widget, Boolean>getOrderedRules(){
        createCollection();
        HashMap<Widget,Boolean> ruleResults= new HashMap<>();
        for(Widget child : DuelArenaRules.duelRules){
            if(child.getChildren()[1].getSpriteId() == 699){
                ruleResults.put(child,true);
            }
            else{
                ruleResults.put(child,false);
            }
        }
        return ruleResults;
    }
    public static boolean ChildNull(Widget child, boolean second){
        if(child == null){
            return false;
        }
        if(child.isSelfHidden()){
            return false;
        }
        if(second) {
            System.out.println(child.getSpriteId());
            if (child.getSpriteId() == 1193) {
                return true;
            }
        }
        return false;
    }
    
    public static HashMap<DuelItem, Boolean> getSecondItems(){
        HashMap<DuelItem,Boolean> ruleResults= new HashMap<>();
        for (int i = 38; i <= 48 ; i++) {
            Widget child = client.getWidget(756,i);
            switch(i){
                case 38:
                    ruleResults.put(new DuelItem(child, "HeadSlot"),ChildNull(child.getChildren()[2],true));
                    break;
                case 39:
                    ruleResults.put(new DuelItem(child, "CapeSlot"),ChildNull(child.getChildren()[2], true));
                    break;
                case 40:
                    ruleResults.put(new DuelItem(child, "NeckSlot"),ChildNull(child.getChildren()[2],true));
                    break;
                case 41:
                    ruleResults.put(new DuelItem(child, "MainSlot"),ChildNull(child.getChildren()[2],true));
                    break;
                case 42:
                    ruleResults.put(new DuelItem(child, "ChestSlot"),ChildNull(child.getChildren()[2],true));
                    break;
                case 43:
                    ruleResults.put(new DuelItem(child, "ShieldSlot"),ChildNull(child.getChildren()[2],true));
                    break;
                case 44:
                    ruleResults.put(new DuelItem(child, "LegSlot"),ChildNull(child.getChildren()[2],true));
                    break;
                case 45:
                    ruleResults.put(new DuelItem(child, "HandSlot"),ChildNull(child.getChildren()[2],true));
                    break;
                case 46:
                    ruleResults.put(new DuelItem(child, "FeetSlot"),ChildNull(child.getChildren()[2],true));
                    break;
                case 47:
                    ruleResults.put(new DuelItem(child, "RingSlot"),ChildNull(child.getChildren()[2],true));
                    break;
                case 48:
                    ruleResults.put(new DuelItem(child, "ArrowSlot"),ChildNull(child.getChildren()[2],true));
                    break;

            }
        }
        return ruleResults;
    }

    public static HashMap<DuelItem, Boolean> getFirstItems(){
        HashMap<DuelItem,Boolean> ruleResults= new HashMap<>();
        for (int i = 48; i <= 58 ; i++) {
            Widget child = client.getWidget(755,i);
            Widget decider = client.getWidget(755,i+23);
            switch(i){
                case 48:
                    ruleResults.put(new DuelItem(child, "HeadSlot"),ChildNull(decider,false));
                    break;
                case 49:
                    ruleResults.put(new DuelItem(child, "CapeSlot"),ChildNull(decider,false));
                    break;
                case 50:
                    ruleResults.put(new DuelItem(child, "NeckSlot"),ChildNull(decider,false));
                    break;
                case 51:
                    ruleResults.put(new DuelItem(child, "MainSlot"),ChildNull(decider,false));
                    break;
                case 52:
                    ruleResults.put(new DuelItem(child, "ChestSlot"),ChildNull(decider,false));
                    break;
                case 53:
                    ruleResults.put(new DuelItem(child, "ShieldSlot"),ChildNull(decider,false));
                    break;
                case 54:
                    ruleResults.put(new DuelItem(child, "LegSlot"),ChildNull(decider,false));
                    break;
                case 55:
                    ruleResults.put(new DuelItem(child, "HandSlot"),ChildNull(decider,false));
                    break;
                case 56:
                    ruleResults.put(new DuelItem(child, "FeetSlot"),ChildNull(decider,false));
                    break;
                case 57:
                    ruleResults.put(new DuelItem(child, "RingSlot"),ChildNull(decider,false));
                    break;
                case 58:
                    ruleResults.put(new DuelItem(child, "ArrowSlot"),ChildNull(decider,false));
                    break;

            }
        }
        return ruleResults;
    }

    public static List<Widget> GetInventoryItems(){

        return Arrays.stream(client.getWidget(756,13).getDynamicChildren()).filter(Objects::nonNull).filter(x -> x.getItemId() != 6512 && x.getItemId() != -1).collect(Collectors.toList());
    }
    //This function is intended to be called on the 2nd duel screen
    public static List<Widget> GetWornItems(){
    List<Widget> results = new ArrayList<>();
        for (int i = 20; i <= 30 ; i++) {
            results.add(client.getWidget(756,i));
        }
        return results;
    }



}
