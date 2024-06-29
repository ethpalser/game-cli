package com.github.ethpalser.menu.event;

/**
 * EventListener objects register to an event and process the event when it occurs.
 */
public interface EventListener {

    /**
     * Performs an action when the EventType this EventListener is tied receives an Event of that type.
     * Event is a record that provides context for when the EventType occurred.
     *
     * @param event Event representing a record of information for an EventType, including the EventType
     */
    void handleEvent(Event event);

}
