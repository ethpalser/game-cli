package com.github.ethpalser.ui;

import java.io.BufferedWriter;
import java.io.IOException;

public class CommandMenuWriter {

    private final static String WRITER_CLOSED_ERROR_MESSAGE = "writer closed";

    private final BufferedWriter bw;
    private boolean canWrite;

    public CommandMenuWriter(final BufferedWriter ioWriter) {
        this.bw = ioWriter;
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
