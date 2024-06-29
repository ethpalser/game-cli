package com.github.ethpalser.menu;

import com.github.ethpalser.menu.event.Event;
import com.github.ethpalser.menu.event.Result;
import com.github.ethpalser.menu.event.Selectable;

public class SelectableMenu<T> extends SimpleMenu<T> implements Selectable {

    private boolean isAvailable;

    public SelectableMenu(String name, boolean isVisible, T display, boolean isAvailable, Menu... children) {
        super(name, isVisible, display, children);
        this.isAvailable = isAvailable;
    }

    public SelectableMenu(String name, boolean isVisible, T display, boolean isAvailable) {
        this(name, isVisible, display, isAvailable, (Menu[]) null);
        this.isAvailable = true;
    }

    public SelectableMenu(String name, boolean isVisible, T display) {
        this(name, isVisible, display, true);
    }

    public SelectableMenu(final String name, final Menu[] children) {
        super(name, children);
        this.isAvailable = false;
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public Result onSelect() {
        return new Result();
    }

    @Override
    public Result handleEvent(Event event, String[] args) {
        if (event == null) {
            return new Result();
        }
        return switch (event) {
            case RENDER -> this.onRender();
            case SELECT -> this.onSelect();
            default -> new Result();
        };
    }
}
