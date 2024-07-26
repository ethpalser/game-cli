package com.ethpalser.cli.menu.event;

public final class Result {

    public static final String SUCCESS_MESSAGE = "event success";
    public static final String MISSING_MESSAGE = "event ignored, as an event listener is missing";
    public static final String INVALID_MESSAGE = "event failed, as null or illegal event provided";
    public static final String ERROR_MESSAGE = "event failed, as an error occurred";

    private final String message;
    private final boolean hasError;

    public Result(final String message, final boolean hasError) {
        this.message = message;
        this.hasError = hasError;
    }

    public Result(final boolean hasError) {
        this.hasError = hasError;
        if (hasError) {
            this.message = SUCCESS_MESSAGE;
        } else {
            this.message = ERROR_MESSAGE;
        }
    }

    public String getMessage() {
        return this.message;
    }

    public boolean hasError() {
        return hasError;
    }
}
