package com.example.EthanApiPlugin.Collections;

import lombok.Getter;
import net.runelite.api.widgets.Widget;

public class DuelItem {
    @Getter
    private Widget Clickable;
    @Getter
    private String Name;
    public DuelItem(Widget widg, String Name){
       this.Clickable = widg;
       this.Name = Name;
    }
}
