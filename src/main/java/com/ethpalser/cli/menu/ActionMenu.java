package com.ethpalser.cli.menu;

import com.ethpalser.cli.menu.event.EventType;

public abstract class ActionMenu extends Menu {

    protected ActionMenu(String name, String altDisplayString, MenuItem[] children) {
        super(name, altDisplayString, children);
        this.addEventListener(EventType.PRE_RENDER, event -> {
            if (EventType.PRE_RENDER == event.getEventType()) {
                this.updateDisplay();
            }
        });
    }

    protected ActionMenu(String name, MenuItem[] children) {
        this(name, name, children);
    }

    protected ActionMenu(String name) {
        this(name, new MenuItem[]{});
    }

    /**
     * This method executes whenever this Menu receives a PRE_RENDER event, as an EventListener has been added by
     * default wrapping this method. This method is expected to update this menu's display elements,
     * including display text.
     */
    public abstract void updateDisplay();

}
