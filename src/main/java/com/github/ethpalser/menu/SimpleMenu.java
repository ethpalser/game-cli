package com.github.ethpalser.menu;

import com.github.ethpalser.menu.event.Displayable;
import com.github.ethpalser.menu.event.Event;
import com.github.ethpalser.menu.event.Result;

public class SimpleMenu<T> extends AbstractMenu implements Displayable<T> {

    private boolean isVisible;
    private T display;

    public SimpleMenu(String name, boolean isVisible, T display, AbstractMenu... children) {
        super(name, children);
        this.isVisible = isVisible;
        this.display = display;
        this.addChildren(children);
    }

    public SimpleMenu(String name, boolean isVisible, T display) {
        this(name, isVisible, display, new AbstractMenu[]{});
    }

    public SimpleMenu(final String name, final AbstractMenu[] children) {
        super(name, children);
        this.isVisible = false;
        this.display = null;
    }

    @Override
    public Result handleEvent(Event event, String[] args) {
        if (event == null) {
            return new Result();
        }
        return switch (event) {
            case PRE_RENDER -> preRender();
            case RENDER -> onRender();
            case POST_RENDER -> postRender();
            default -> new Result();
        };
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
