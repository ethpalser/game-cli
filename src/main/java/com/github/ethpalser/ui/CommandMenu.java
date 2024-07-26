package com.github.ethpalser.ui;

import com.github.ethpalser.Context;
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

    private final Context context;
    private Menu main;
    private CommandMenuReader reader;
    private CommandMenuWriter writer;

    public CommandMenu(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        this.context = context;
    }

    public CommandMenu(Context context, Menu main) {
        this(context);
        this.main = main;
        this.context.setMenu(this.main);
    }

    private Menu getActive() {
        return this.context.getMenu();
    }

    private void setActive(Menu menu) {
        this.context.setMenu(menu);
    }

    private Set<String> getEscapeCommands() {
        return Set.of("exit", "close", "quit", "q");
    }

    private String awaitInput() {
        if (reader == null) {
            return null;
        }
        try {
            return this.reader.readOption(this.getActive().getChildren().keySet().stream().toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void awaitOutput(String message) {
        if (writer == null) {
            return;
        }
        try {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        this.reader = new CommandMenuReader(new BufferedReader(new InputStreamReader(System.in)));
        this.writer = new CommandMenuWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        reader.setErrorWriter(this.writer.getBufferedWriter());

        boolean close = false;
        do {
            if (this.getActive() == null) {
                this.setActive(this.main);
            }
            do {
                this.sendEvent(new Event(EventType.PRE_RENDER), this.getActive());
                this.awaitOutput(this.getActive().getTextDisplay());
                this.sendEvent(new Event(EventType.RENDER), this.getActive());
                this.sendEvent(new Event(EventType.POST_RENDER), this.getActive());

                String input = this.awaitInput();
                if (input == null || this.getEscapeCommands().contains(input)) {
                    close = true;
                    break;
                }
                // Split the input, as it may have arguments and would not match a menu option
                String[] split = input.split("\\s+");
                MenuItem selected = this.getActive().getChild(split[0]);
                if (selected != null) {
                    this.sendEvent(new Event(EventType.EXECUTE, input), selected);
                }
            } while (!this.context.refresh());
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
