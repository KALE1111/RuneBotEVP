package com.example.RuneBotApi;

import javax.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;


@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SpecialMenuOptionClicked {
    /**
     * The clicked menu entry
     */
    @Getter
    private final MenuEntry menuEntry;

    /**
     * Whether or not the event has been consumed by a subscriber.
     */
    @Getter
    @Setter
    private boolean consumed;

    /**
     * Action parameter 0. Its value depends on the menuAction.
     */
    @EqualsAndHashCode.Include
    @ToString.Include
    public int getParam0()
    {
        return menuEntry.getParam0();
    }

    public void setParam0(int param0)
    {
        menuEntry.setParam0(param0);
    }

    /**
     * Action parameter 1. Its value depends on the menuAction.
     */
    @EqualsAndHashCode.Include
    @ToString.Include
    public int getParam1()
    {
        return menuEntry.getParam1();
    }

    public void setParam1(int param1)
    {
        menuEntry.setParam1(param1);
    }

    /**
     * The option text added to the menu.
     */
    @EqualsAndHashCode.Include
    @ToString.Include
    public String getMenuOption()
    {
        return menuEntry.getOption();
    }

    public void setMenuOption(String menuOption)
    {
        menuEntry.setOption(menuOption);
    }

    /**
     * The target of the action.
     */
    @EqualsAndHashCode.Include
    @ToString.Include
    public String getMenuTarget()
    {
        return menuEntry.getTarget();
    }

    public void setMenuTarget(String menuTarget)
    {
        menuEntry.setTarget(menuTarget);
    }

    /**
     * The action performed.
     */
    @EqualsAndHashCode.Include
    @ToString.Include
    public MenuAction getMenuAction()
    {
        return menuEntry.getType();
    }

    public void setMenuAction(MenuAction menuAction)
    {
        menuEntry.setType(menuAction);
    }

    /**
     * The ID of the object, actor, or item that the interaction targets.
     */
    @EqualsAndHashCode.Include
    @ToString.Include
    public int getId()
    {
        return menuEntry.getIdentifier();
    }

    public void setId(int id)
    {
        menuEntry.setIdentifier(id);
    }

    /**
     * Test if this menu entry is an item op. "Use" and "Examine" are not considered item ops.
     * @return
     */
    public boolean isItemOp()
    {
        return menuEntry.isItemOp();
    }

    /**
     * If this menu entry is an item op, get the item op id
     * @return 1-5
     */
    public int getItemOp()
    {
        return menuEntry.getItemOp();
    }

    /**
     * If this menu entry is an item op, get the item id
     * @return
     * @see net.runelite.api.ItemID
     * @see net.runelite.api.NullItemID
     */
    public int getItemId()
    {
        return menuEntry.getItemId();
    }

    /**
     * Get the widget this menu entry is on, if this is a menu entry
     * with an associated widget. Such as eg, CC_OP.
     * @return
     */
    @Nullable
    public Widget getWidget()
    {
        return menuEntry.getWidget();
    }

    /**
     * Marks the event as having been consumed.
     * <p>
     * Setting this state indicates that a plugin has processed the menu
     * option being clicked and that the event will not be passed on
     * for handling by vanilla client code.
     */
    public void consume()
    {
        this.consumed = true;
    }

    @Deprecated
    public int getActionParam()
    {
        return menuEntry.getParam0();
    }

    @Deprecated
    public void setActionParam(int actionParam)
    {
        menuEntry.setParam0(actionParam);
    }

    @Deprecated
    public int getWidgetId()
    {
        return menuEntry.getParam1();
    }

    @Deprecated
    public void setWidgetId(int widgetId)
    {
        menuEntry.setParam1(widgetId);
    }


    @Deprecated
    public void setMenuEntry(MenuEntry entry)
    {
        this.setMenuOption(entry.getOption());
        this.setMenuTarget(entry.getTarget());
        this.setId(entry.getIdentifier());
        this.setMenuAction(entry.getType());
        this.setParam0(entry.getParam0());
        this.setParam1(entry.getParam1());
    }
}
