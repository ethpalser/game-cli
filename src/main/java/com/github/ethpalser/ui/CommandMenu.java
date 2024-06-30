package com.github.ethpalser.ui;

import com.github.ethpalser.menu.Menu;
import com.github.ethpalser.menu.MenuItem;
import com.github.ethpalser.menu.event.Event;
import com.github.ethpalser.menu.event.EventType;
import com.github.ethpalser.menu.event.Result;
import java.util.Set;

public class CommandMenu implements Runnable {

    private final Menu main;
    private Menu active;
    private boolean activeUpdated;

    public CommandMenu(Menu main) {
        this.main = main;
        this.active = main;
        this.activeUpdated = false;
    }

    private void setActiveMenu(final Menu menu) {
        this.active = menu;
        this.activeUpdated = true;
    }

    private Set<String> getEscapeCommands() {
        return Set.of("exit", "close", "quit", "q");
    }

    private void sendEvent(Event event, MenuItem receiver) {
        Result result = receiver.receiveEvent(event);
        if (result.hasError()) {
            System.err.println(result.getMessage());
        }
    }

    /**
     * Establish open streams and run main loop.
     */
    public void open() {
        // todo: set up io
        boolean close = false;
        do {
            if (this.active == null) {
                this.setActiveMenu(this.main);
            }
            this.sendEvent(new Event(EventType.PRE_RENDER), this.active);
            do {
                this.sendEvent(new Event(EventType.RENDER), this.active);
                this.sendEvent(new Event(EventType.POST_RENDER), this.active);
                // todo: await input from io
                String input = "";
                if (this.getEscapeCommands().contains(input)) {
                    close = true;
                    break;
                }
                MenuItem selected = this.active.getChildren().get("");
                if (this.active.getName().equals(selected.getName())) {
                    this.sendEvent(new Event(EventType.SELECT), selected);
                    if (selected instanceof Menu) {
                        this.active = (Menu) selected;
                        this.activeUpdated = true;
                    }
                }
                this.sendEvent(new Event(EventType.EXECUTE), selected);
            } while (!this.activeUpdated);
        } while (!close);
        // todo: clean up io
    }

    @Override
    public void run() {
        this.open();
    }
}
