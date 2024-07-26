package com.ethpalser.cli.menu;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Menu objects are containers of data that can contain sub-menus (children). These objects can handle events
 * that it supports.
 */
public class Menu extends MenuItem {

    private final Map<String, MenuItem> children;

    public Menu(final String name, final String altDisplayString, final MenuItem[] children) {
        super(name, altDisplayString);
        this.children = new LinkedHashMap<>();
        this.addChildren(children);
    }

    public Menu(final String name, final MenuItem[] children) {
        this(name, name, children);
    }

    public Menu(final String name) {
        this(name, name, new MenuItem[]{});
    }

    /**
     * Returns this Menu's children.
     *
     * @return Map of Menu name to Menu
     */
    public Map<String, MenuItem> getChildren() {
        return this.children;
    }

    /**
     * Returns a single child Menu that matches the given String. If there is no match, null will be returned.
     * The name is converted to lower case, as names are stored using only lower case.
     *
     * @param name String representing name of a Menu
     * @return Menu
     */
    public MenuItem getChild(String name) {
        return this.children.get(name.toLowerCase(Locale.ROOT));
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
    public MenuItem getChild(int index) {
        if (this.children.size() <= index) {
            return null;
        }
        return (MenuItem) this.children.values().toArray()[index];
    }

    /**
     * Add one or more Menu objects. The key used is the Menu object's name
     *
     * @param children array of Menu objects
     */
    public void addChildren(MenuItem... children) {
        for (MenuItem child : children) {
            if (child.getName() == null) {
                throw new IllegalArgumentException("MenuItem child's name cannot be null");
            }
            this.children.put(child.getName().toLowerCase(Locale.ROOT), child);
        }
    }

    /**
     * Add a single Menu object.
     *
     * @param child Menu object
     */
    public void addChild(MenuItem child) {
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
