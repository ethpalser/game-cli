package com.github.ethpalser.menu;

import com.github.ethpalser.menu.event.Displayable;
import com.github.ethpalser.menu.event.Event;
import com.github.ethpalser.menu.event.Result;

public class SimpleMenu<T> extends Menu implements Displayable<T> {

    private boolean isVisible;
    private T display;

    public SimpleMenu(String name, boolean isVisible, T display, Menu... children) {
        super(name, children);
        this.isVisible = isVisible;
        this.display = display;
        this.addChildren(children);
    }

    public SimpleMenu(String name, boolean isVisible, T display) {
        this(name, isVisible, display, new Menu[]{});
    }

    public SimpleMenu(final String name, final Menu[] children) {
        super(name, children);
        this.isVisible = false;
        this.display = null;
    }

    @Override
    public Result handleEvent(Event event) {
        if (Event.DISPLAY == event) {
            return onRender();
        }
        return new Result();
    }

    @Override
    public boolean isVisible() {
        return this.isVisible;
    }

    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public T getDisplay() {
        return this.display;
    }

    @Override
    public void setDisplay(T display) {
        this.display = display;
    }

}
