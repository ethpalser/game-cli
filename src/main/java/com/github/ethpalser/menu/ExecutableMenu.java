package com.github.ethpalser.menu;

import com.github.ethpalser.menu.event.EventType;
import com.github.ethpalser.menu.event.Executable;
import com.github.ethpalser.menu.event.Result;

public class ExecutableMenu<T> extends SelectableMenu<T> implements Executable {

    public ExecutableMenu(String name, boolean isVisible, T display, boolean isAvailable,
            AbstractMenu... children) {
        super(name, isVisible, display, isAvailable, children);
    }

    public ExecutableMenu(String name, boolean isVisible, T display, boolean isAvailable) {
        this(name, isVisible, display, isAvailable, (AbstractMenu[]) null);
    }

    public ExecutableMenu(String name, boolean isVisible, T display) {
        this(name, isVisible, display, true);
    }

    public ExecutableMenu(final String name, final AbstractMenu[] children) {
        super(name, children);
    }

    @Override
    public Result onExecute(String[] args) {
        return new Result();
    }

    @Override
    public Result handleEvent(EventType eventType, String[] args) {
        if (eventType == null) {
            return new Result();
        }
        return switch (eventType) {
            case PRE_RENDER -> this.preRender();
            case RENDER -> this.onRender();
            case POST_RENDER -> this.postRender();
            case SELECT -> this.onSelect();
            case EXECUTE -> this.onExecute(args);
        };
    }
}
