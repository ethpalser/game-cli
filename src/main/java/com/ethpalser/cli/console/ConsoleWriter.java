package com.ethpalser.cli.console;

import java.io.BufferedWriter;
import java.io.IOException;

public class ConsoleWriter {

    private static final String WRITER_CLOSED_ERROR_MESSAGE = "writer closed";

    private final BufferedWriter bw;
    private boolean canWrite;

    public ConsoleWriter(final BufferedWriter ioWriter) {
        this.bw = ioWriter;
        this.canWrite = ioWriter != null;
    }

    /**
     * Writes a string with the Writer. This can write as long as the writer has not been closed. If the writer is
     * closed an IOException is thrown.
     *
     * @param message String to write
     * @throws IOException An I/O exception occurred with the Writer or the Writer is closed.
     */
    public void write(String message) throws IOException {
        if (!canWrite) {
            throw new IOException(WRITER_CLOSED_ERROR_MESSAGE);
        }
        this.bw.write(message);
        this.bw.flush();
    }

    public void close() throws IOException {
        this.canWrite = false;
        this.bw.close();
    }

}
