package com.github.ethpalser.menu;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Menu objects are containers of data that can contain sub-menus (children). These objects can handle events
 * that it supports.
 */
public class Menu extends MenuItem {

    private final Map<String, Menu> children;

    private String textDisplay; // alternate to display for screen readers, or primary display as string

    public Menu(final String name, final String altDisplayString, final Menu[] children) {
        super(name, altDisplayString);
        this.children = new LinkedHashMap<>();
        this.addChildren(children);
    }

    public Menu(final String name, final Menu[] children) {
        this(name, "undefined", children);
    }

    public Menu(final String name) {
        this(name, "undefined", new Menu[]{});
    }

    /**
     * Returns this Menu's children.
     *
     * @return Map of Menu name to Menu
     */
    public Map<String, Menu> getChildren() {
        return this.children;
    }

    /**
     * Returns a single child Menu that matches the given String. If there is no match, null will be returned.
     *
     * @param name String representing name of a Menu
     * @return Menu
     */
    public Menu getChild(String name) {
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
    public Menu getChild(int index) {
        if (this.children.size() <= index) {
            return null;
        }
        return (Menu) this.children.values().toArray()[index];
    }

    /**
     * Add one or more Menu objects.
     *
     * @param children array of Menu objects
     */
    public void addChildren(Menu... children) {
        for (Menu child : children) {
            this.children.put(child.getName(), child);
        }
    }

    /**
     * Add a single Menu object.
     *
     * @param child Menu object
     */
    public void addChild(Menu child) {
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

}
