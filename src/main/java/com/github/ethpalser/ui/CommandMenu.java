package com.github.ethpalser.ui;

import com.github.ethpalser.menu.Menu;
import com.github.ethpalser.menu.MenuItem;
import com.github.ethpalser.menu.event.Event;
import com.github.ethpalser.menu.event.EventType;
import com.github.ethpalser.menu.event.Result;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
        CommandMenuReader reader = new CommandMenuReader(new BufferedReader(new InputStreamReader(System.in)));
        CommandMenuWriter writer = new CommandMenuWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        reader.setErrorWriter(writer.getBufferedWriter());

        boolean close = false;
        do {
            if (this.active == null) {
                this.setActiveMenu(this.main);
            }
            this.sendEvent(new Event(EventType.PRE_RENDER), this.active);
            do {
                try {
                    writer.write(this.active.getTextDisplay());
                } catch (IOException e) {
                    e.printStackTrace();
                    close = true;
                    break;
                }

                this.sendEvent(new Event(EventType.RENDER), this.active);
                this.sendEvent(new Event(EventType.POST_RENDER), this.active);

                String input = "";
                try {
                    input = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

        try {
            reader.close();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.open();
    }
}
