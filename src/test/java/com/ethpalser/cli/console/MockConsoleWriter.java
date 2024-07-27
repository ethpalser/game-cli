package com.ethpalser.cli.console;

public class MockConsoleWriter extends ConsoleWriter {

    public MockConsoleWriter() {
        super(null);
    }

    @Override
    public void write(String message) {
        // Do nothing
    }

    @Override
    public void close() {
        // Do nothing
    }
}
