package com.github.ethpalser.menu;

import com.github.ethpalser.menu.event.Event;
import com.github.ethpalser.menu.event.EventListener;
import com.github.ethpalser.menu.event.EventType;
import com.github.ethpalser.menu.event.Result;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MenuItem {

    private final String name;
    private final Map<EventType, EventListener> eventListeners;

    private String textDisplay; // alternate to display for screen readers, or primary display as string

    public MenuItem(final String name, final String altDisplayString) {
        this.name = name;
        this.textDisplay = altDisplayString;
        this.eventListeners = new HashMap<>();
    }

    public MenuItem(final String name) {
        this(name, "undefined");
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
