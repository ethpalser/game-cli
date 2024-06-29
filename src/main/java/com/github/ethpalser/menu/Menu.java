package com.github.ethpalser.menu;

import com.github.ethpalser.menu.event.Event;
import com.github.ethpalser.menu.event.EventListener;
import com.github.ethpalser.menu.event.EventType;
import com.github.ethpalser.menu.event.Result;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Menu objects are containers of data that can contain sub-menus (children). These objects can handle events
 * that it supports.
 */
public class Menu {

    private final String name;
    private final Map<String, Menu> children;
    private final Map<EventType, EventListener> eventListeners;

    private String textDisplay; // alternate to display for screen readers, or primary display as string

    protected Menu(final String name, final Menu[] children, final String altDisplayString) {
        this.name = name;
        this.children = new LinkedHashMap<>();
        this.textDisplay = altDisplayString;
        this.eventListeners = new HashMap<>();
        this.addChildren(children);
    }

    protected Menu(final String name, final Menu[] children) {
        this.name = name;
        this.children = new LinkedHashMap<>();
        this.eventListeners = new HashMap<>();
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
     * Returns a representation of its graphical display as a string. For string-only contexts, this would be the
     * primary display. By default, nothing is ensuring an accurate representation is used, and this may simply be
     * used to describe a graphic for screen readers.
     *
     * @return String
     */
    public String getTextDisplay() {
        return this.textDisplay;
    }

    /**
     * Sets the alternate, string display of this Menu.
     *
     * @param stringDisplay Representation of its graphical display as a string.
     */
    public void setTextDisplay(String stringDisplay) {
        this.textDisplay = stringDisplay;
    }

    /**
     * Determines if this displayable object should be prevented from being rendered.
     *
     * @return boolean (true/false)
     */
    public boolean isHidden() {
        return false;
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

    /**
     * Remove up to one Menu object, if this Menu contains a Menu with that name
     *
     * @param name String representing a Menu's name
     */
    public void removeChild(String name) {
        removeChildren(name);
    }

    /**
     * Returns all event listeners of this Menu as a list
     *
     * @return List of EventListener
     * @see EventListener
     */
    public List<EventListener> getEventListeners() {
        return this.eventListeners.values().stream().toList();
    }

    /**
     * Returns a single EventListener tied to a specific EventType. If there is no EventListener, an empty
     * Optional object will be returned.
     *
     * @param eventType EventType the EventListener is on.
     * @return Optional of EventListener
     */
    public Optional<EventListener> getEventListener(EventType eventType) {
        return Optional.ofNullable(this.eventListeners.get(eventType));
    }

    /**
     * Connect an EventListener to an EventType. If there is already a listener on that EventType, it will be replaced.
     *
     * @param eventType     EventType
     * @param eventListener EventListener
     */
    public void addEventListener(EventType eventType, EventListener eventListener) {
        this.eventListeners.put(eventType, eventListener);
    }

    /**
     * Removes an EventListener on an EventType. If there is no listener on that EventType, nothing will change.
     *
     * @param eventType EventType
     */
    public void removeEventListener(EventType eventType) {
        this.eventListeners.remove(eventType);
    }

    /**
     * Determines if this Menu should not be interacted with
     *
     * @return boolean; true if
     */
    public boolean isDisabled() {
        return Optional.ofNullable(this.getEventListeners()).orElse(List.of()).isEmpty();
    }

    /**
     * Accepts an event and defers handling to an event listener for that event. All Exceptions are caught and
     * returned as a Result containing an error and the exception message. Additionally, Results with an error
     * are returned for any case where the event cannot be processed, except if no listener is registered for the event.
     *
     * @param event Event
     * @return Result
     */
    public Result receiveEvent(Event event) {
        if (event == null || event.getEventType() == null) {
            return new Result("cannot resolve null event", true);
        }
        try {
            this.getEventListener(event.getEventType()).ifPresent(listener -> listener.handleEvent(event));
        } catch (Exception ex) {
            return new Result(ex.getMessage(), true);
        }
        return new Result("event processed", false);
    }
}
