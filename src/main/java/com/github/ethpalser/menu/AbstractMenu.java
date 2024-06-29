package com.github.ethpalser.menu;

import com.github.ethpalser.menu.event.EventType;
import com.github.ethpalser.menu.event.Result;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Menu objects are containers of data that can contain sub-menus (children). These objects can handle events
 * that it supports.
 */
public abstract class AbstractMenu {

    private final String name;
    private final Map<String, AbstractMenu> children;

    protected AbstractMenu(final String name, final AbstractMenu[] children) {
        this.name = name;
        this.children = new LinkedHashMap<>();
        this.addChildren(children);
    }

    /**
     * Returns this menu's name.
     *
     * @return String representing its name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Handles an event sent to this object. The result contains data that may be useful for the event emitter.
     * It is recommended to return an empty Result instead of null for events that are not handled.
     *
     * @param eventType Event that is to be handled
     * @return Result of the event
     * @see EventType
     * @see Result
     */
    public abstract Result handleEvent(EventType eventType, String[] args);

    /**
     * Returns this Menu's children.
     *
     * @return Map of Menu name to Menu
     */
    public Map<String, AbstractMenu> getChildren() {
        return this.children;
    }

    /**
     * Returns a single child Menu that matches the given String. If there is no match, null will be returned.
     *
     * @param name String representing name of a Menu
     * @return Menu
     */
    public AbstractMenu getChild(String name) {
        return this.children.get(name);
    }

    /**
     * Return a single child Menu located at the given index. If the index is out of bounds, null will be returned.
     * <br><br>
     * Notice: There may not be an order to the elements, as each Map converted to an array my represent the order
     * differently. As a result, the returned Menu may not be what is expected.
     *
     * @param index integer representing the location in the Menu-storing data structure.
     * @return Menu
     */
    public AbstractMenu getChild(int index) {
        if (this.children.size() <= index) {
            return null;
        }
        return (AbstractMenu) this.children.values().toArray()[index];
    }

    /**
     * Add one or more Menu objects.
     *
     * @param children array of Menu objects
     */
    public void addChildren(AbstractMenu... children) {
        for (AbstractMenu child : children) {
            this.children.put(child.getName(), child);
        }
    }

    /**
     * Add a single Menu object.
     *
     * @param child Menu object
     */
    public void addChild(AbstractMenu child) {
        addChildren(child);
    }

    /**
     * Remove up to one or more Menu objects, if this Menu contains a Menu with these names.
     *
     * @param names String array representing a list of Menu names
     */
    public void removeChildren(String... names) {
        for (String childName : names) {
            this.children.remove(childName);
        }
    }

    /**
     * Remove up to one Menu object, if this Menu contains a Menu with that name
     *
     * @param name String representing a Menu's name
     */
    public void removeChild(String name) {
        removeChildren(name);
    }

}
