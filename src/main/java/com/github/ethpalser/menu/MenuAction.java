package com.github.ethpalser.menu;

import com.github.ethpalser.menu.event.EventType;

/**
 * An abstract definition of a MenuItem that wraps adding an EventListener for EventType.EXECUTE events.
 */
public abstract class MenuAction extends MenuItem {

    protected MenuAction(String name, String altDisplayString) {
        super(name, altDisplayString);
        this.addEventListener(EventType.EXECUTE, event -> {
            if (!isValid(event.getCommandString())) {
                throw new IllegalArgumentException(getInvalidMessage());
            }
            execute(event.getCommandString());
        });
    }

    protected MenuAction(String name) {
        this(name, name);
    }

    /**
     * Fetches a message to display when isValid is false.
     *
     * @return String
     */
    public String getInvalidMessage() {
        return "";
    }

    /**
     * Validates the given command string to ensure it can be used for execute.
     *
     * @param commandString String representing input received from IO and used for execute().
     * @return boolean
     */
    public boolean isValid(String commandString) {
        return true;
    }

    /**
     * Performs an action with a given string to customize output
     *
     * @param commandString String representing input received from IO.
     */
    public abstract void execute(String commandString);

}
