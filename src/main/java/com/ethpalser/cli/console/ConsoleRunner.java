package com.ethpalser.cli.console;

import com.ethpalser.cli.menu.Context;
import com.ethpalser.cli.menu.Menu;
import com.ethpalser.cli.menu.MenuItem;
import com.ethpalser.cli.menu.event.Event;
import com.ethpalser.cli.menu.event.EventType;
import com.ethpalser.cli.menu.event.Result;
import com.ethpalser.cli.menu.exception.InvalidContextException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

public class ConsoleRunner {

    private static final List<String> CONFIRM_OPTIONS = Arrays.asList("n", "y", "no", "yes");

    private final Context context;
    private final ConsoleReader reader;
    private final ConsoleWriter writer;

    public ConsoleRunner() {
        this.context = Context.getInstance();
        this.context.reset();
        this.context.setDefault(null);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        this.writer = new ConsoleWriter(bw);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        this.reader = new ConsoleReader(br, bw);
    }

    public ConsoleRunner(Menu main) {
        this();
        this.context.setDefault(main);
    }

    private String awaitInput(ConsoleReader reader, List<String> options) {
        if (reader == null) {
            return null;
        }
        try {
            return reader.readOption(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void awaitOutput(ConsoleWriter writer, String message) {
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
     * Perform one cycle of sending events and receiving input. This may be used to perform finer control over the
     * runtime by specifying when a cycle should be performed. This method will never close the reader and writer
     * provided to it, as such you should close these separately
     *
     * @param reader ConsoleReader that handles IO reads
     * @param writer ConsoleWriter that handles IO writes
     * @return boolean false if an escape is registered, otherwise true
     */
    public boolean runCycle(ConsoleReader reader, ConsoleWriter writer) throws IllegalArgumentException, IOException,
            InvalidContextException {
        if (reader == null || writer == null) {
            throw new IllegalArgumentException("i/o objects are null");
        }
        if (!reader.ready() || !writer.ready()) {
            throw new IOException("i/o streams closed");
        }
        Menu activeMenu = this.context.peek();
        if (activeMenu == null) {
            throw new InvalidContextException();
        }

        this.sendEvent(new Event(EventType.PRE_RENDER), activeMenu);
        this.awaitOutput(writer, activeMenu.getTextDisplay());
        this.sendEvent(new Event(EventType.RENDER), activeMenu);
        this.sendEvent(new Event(EventType.POST_RENDER), activeMenu);

        List<String> visibleOptions = this.context.peek().getChildren().values()
                .stream().filter(child -> !child.isHidden())
                .map(MenuItem::getName)
                .toList();
        String input = this.awaitInput(reader, visibleOptions);
        if (input == null || reader.getEscapeCommands().contains(input)) {
            writer.write("Closing the program, are you sure? (yes/no)");
            String confirmation = this.awaitInput(reader, CONFIRM_OPTIONS);
            return "n".equalsIgnoreCase(confirmation) || "no".equalsIgnoreCase(confirmation);
        }

        if (reader.getBackCommands().contains(input)) {
            this.context.pop();
        } else {
            // Split the input, as it may have arguments and would not match a menu option
            String[] split = input.split("\\s+");
            MenuItem selected = activeMenu.getChild(split[0]);
            if (selected != null && !selected.isDisabled()) {
                this.sendEvent(new Event(EventType.SELECT, input), selected);
            }
        }
        return true;
    }

    public boolean ready() {
        return this.reader.ready() && this.writer.ready() && this.context.getDefault() != null;
    }

    /**
     * Runs main loop to call runCycle. Closes reader and writer after an escape command is registered in runCycle.
     */
    public void open() {
        boolean canRun = this.ready();
        while (canRun) {
            try {
                // Ready checks are always performed in case the state is changed unexpectedly
                canRun = this.ready() && this.runCycle(this.reader, this.writer);
            } catch (InvalidContextException ex) {
                System.err.println(ex.getMessage());
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        }
        this.close();
    }

    /**
     * Closes reader and writer preventing further input from the user and ends the program.
     */
    public void close() {
        try {
            this.reader.close();
            this.writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
