package com.github.ethpalser;

public final class SelectableResult {

    private final String source;
    private final Selectable next;
    private final String message;
    private final boolean hasError;

    public SelectableResult(final String sourceName, final Selectable next, final String message, final boolean hasError) {
        this.source = sourceName;
        this.next = next;
        this.message = message;
        this.hasError = hasError;
    }

    public SelectableResult(final String sourceName, final Selectable next, final String message) {
        this(sourceName, next, message, false);
    }

    public SelectableResult(final String sourceName, final Selectable next) {
        this(sourceName, next, null, false);
    }

    public Selectable getNext() {
        return this.next;
    }

    public String getSource() {
        return this.source;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean hasError() {
        return this.hasError;
    }
}
