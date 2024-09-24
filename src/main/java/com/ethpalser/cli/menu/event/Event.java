package com.ethpalser.cli.menu.event;

public class Event {

    private final EventType eventType;
    private final String command;
    private final String[] args;

    public Event(EventType eventType, String command) {
        this(eventType, command, null);
    }

    public Event(EventType eventType, String command, String[] args) {
        this.eventType = eventType;
        this.command = command;
        this.args = args;
    }

    public Event(EventType eventType) {
        this(eventType, null);
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public String getCommand() {
        return this.command;
    }

    public String[] getArgs() {
        return this.args;
    }
}
