package com.github.ethpalser.menu;

import com.github.ethpalser.menu.event.Event;
import com.github.ethpalser.menu.event.Executable;
import com.github.ethpalser.menu.event.Result;

public class ExecutableMenu<T> extends SelectableMenu<T> implements Executable {

    public ExecutableMenu(String name, boolean isVisible, T display, boolean isAvailable,
            Menu... children) {
        super(name, isVisible, display, isAvailable, children);
    }

    public ExecutableMenu(String name, boolean isVisible, T display, boolean isAvailable) {
        this(name, isVisible, display, isAvailable, (Menu[]) null);
    }

    public ExecutableMenu(String name, boolean isVisible, T display) {
        this(name, isVisible, display, true);
    }

    public ExecutableMenu(final String name, final Menu[] children) {
        super(name, children);
    }

    @Override
    public Result onExecute(String[] args) {
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
            case EXECUTE -> this.onExecute(args);
        };
    }
}
