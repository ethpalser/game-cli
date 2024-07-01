package com.github.ethpalser.menu;

import com.github.ethpalser.menu.event.EventType;

public abstract class ActionMenu extends Menu {

    protected ActionMenu(String name, String altDisplayString, MenuItem[] children) {
        super(name, altDisplayString, children);
        this.addEventListener(EventType.PRE_RENDER, event -> {
            if (EventType.PRE_RENDER == event.getEventType()) {
                this.refreshDisplay();
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
     * A wrapper for setTextDisplay that is used be this object's default PRE_REFRESH EventListener. It is preferred
     * to override this method over setTextDisplay for making
     */
    abstract void refreshDisplay();

}
