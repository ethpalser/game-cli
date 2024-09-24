package com.ethpalser.cli.console;

import com.ethpalser.cli.menu.Context;
import com.ethpalser.cli.menu.Menu;
import com.ethpalser.cli.menu.MenuItem;
import com.ethpalser.cli.menu.event.Event;
import com.ethpalser.cli.menu.event.EventType;
import com.ethpalser.cli.menu.event.Result;
import com.ethpalser.cli.menu.exception.InvalidContextException;
import com.ethpalser.cli.util.Pair;
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

    private Pair<String, String[]> awaitInput(ConsoleReader reader, List<String> options) {
        if (reader == null) {
            return new Pair<>("", null);
        }
        try {
            return reader.readOption(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pair<>("", null);
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
        Pair<String, String[]> input = this.awaitInput(reader, visibleOptions);
        if (input == null || reader.getEscapeCommands().contains(input.getFirst())) {
            writer.write("Closing the program, are you sure? (yes/no)");
            String confirm = this.awaitInput(reader, CONFIRM_OPTIONS).getFirst();
            boolean close = "y".equalsIgnoreCase(confirm) || "yes".equalsIgnoreCase(confirm);

            if (close && this.context.peek().isSubmitOnLeave()) {
                writer.write("Closing with changes, do you want to save your changes? (yes/no)");
                String doubleConfirm = this.awaitInput(reader, CONFIRM_OPTIONS).getFirst();
                if ("y".equalsIgnoreCase(doubleConfirm) || "yes".equalsIgnoreCase(doubleConfirm)) {
                    this.sendEvent(new Event(EventType.ON_CLOSE, null), this.context.peek());
                }
                return false;
            }
            return !close;
        }

        if (reader.getBackCommands().contains(input.getFirst())) {
            if (this.context.peek().isSubmitOnLeave()) {
                writer.write("Leaving with changes, do you want to save your changes? (yes/no)");
                String confirmation = this.awaitInput(reader, CONFIRM_OPTIONS).getFirst();
                if ("y".equalsIgnoreCase(confirmation) || "yes".equalsIgnoreCase(confirmation)) {
                    this.sendEvent(new Event(EventType.ON_CLOSE, null), this.context.peek());
                }
            }
            this.context.pop();
        } else {
            MenuItem selected = activeMenu.getChild(input.getFirst());
            if (selected != null && !selected.isDisabled()) {
                this.sendEvent(new Event(EventType.SELECT, input.getFirst(), input.getLast()), selected);
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
