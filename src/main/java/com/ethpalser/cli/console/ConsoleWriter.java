package com.ethpalser.cli.console;

import java.io.BufferedWriter;
import java.io.IOException;

public class ConsoleWriter {

    private static final String WRITER_CLOSED_ERROR_MESSAGE = "writer closed";

    private final BufferedWriter bw;
    private boolean canWrite;

    public ConsoleWriter(final BufferedWriter ioWriter) {
        this.bw = ioWriter;
        this.canWrite = true;
    }

    public BufferedWriter getBufferedWriter() {
        return this.bw;
    }

    public void write(String message) throws IOException {
        if (!canWrite) {
            throw new IOException(WRITER_CLOSED_ERROR_MESSAGE);
        }
        this.bw.write(message);
        this.bw.flush();
    }

    public void close() throws IOException {
        this.bw.close();
        this.canWrite = false;
    }

}
