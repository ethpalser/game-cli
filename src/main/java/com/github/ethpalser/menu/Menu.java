package com.github.ethpalser.menu;

import com.github.ethpalser.menu.event.Event;
import com.github.ethpalser.menu.event.Result;
import java.util.Map;
import java.util.TreeMap;

/**
 * Menu objects are containers of data that can contain sub-menus (children). These objects capable of handling events,
 * that it supports.
 */
public abstract class Menu {

    private final String name;
    private final Map<String, Menu> children;

    Menu(final String name, final Menu[] children) {
        this.name = name;
        this.children = new TreeMap<>();
        this.addChildren(children);
    }

    /**
     * Returns this menu's name.
     *
     * @return String representing its name
     */
    String getName() {
        return this.name;
    }

    /**
     * Handles an event sent to this object. The result contains data that may be useful for the event emitter.
     * It is recommended to return an empty Result instead of null for events that are not handled.
     *
     * @param event Event that is to be handled
     * @return Result of the event
     * @see Event
     * @see Result
     */
    abstract Result handleEvent(Event event, String[] args);

    /**
     * Returns this Menu's children.
     *
     * @return Map of Menu name to Menu
     */
    Map<String, Menu> getChildren() {
        return this.children;
    }

    /**
     * Add one or more Menu objects.
     *
     * @param children array of Menu objects
     */
    void addChildren(Menu... children) {
        for (Menu child : children) {
            this.children.put(child.getName(), child);
        }
    }

    /**
     * Add a single Menu object.
     *
     * @param child Menu object
     */
    void addChild(Menu child) {
        addChildren(child);
    }

    /**
     * Remove up to one or more Menu objects, if this Menu contains a Menu with these names.
     *
     * @param names String array representing a list of Menu names
     */
    void removeChildren(String... names) {
        for (String name : names) {
            this.children.remove(name);
        }
    }

    /**
     * Remove up to one Menu object, if this Menu contains a Menu with that name
     *
     * @param name String representing a Menu's name
     */
    void removeChild(String name) {
        removeChildren(name);
    }

}
