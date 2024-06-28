package com.github.ethpalser.menu.event;

public final class Result {

    private final String message;
    private final boolean hasError;

    public Result(final String message, final boolean hasError) {
        this.message = message;
        this.hasError = hasError;
    }

    public Result(final String message) {
        this(message, false);
    }

    public String getMessage() {
        return this.message;
    }

    public boolean hasError() {
        return hasError;
    }
}
