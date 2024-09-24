package com.ethpalser.cli.menu;

import com.ethpalser.cli.menu.event.Event;
import com.ethpalser.cli.menu.event.EventType;
import com.ethpalser.cli.menu.event.Result;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestMenu {

    @Test
    void testReceiveEvent_givenNoEventListener_thenResultMessageIsNotSuccess() {
        Menu menu = new Menu("test");
        Result result = menu.receiveEvent(new Event(EventType.SELECT));
        Assertions.assertFalse(result.hasError());
        // This assumes the result's success message is using the default message for this assertion to pass
        Assertions.assertNotEquals(Result.SUCCESS_MESSAGE, result.getMessage());
    }

    @Test
    void testReceiveEvent_givenEventListenerThrowsException_thenResultHasError() {
        Menu menu = new Menu("test");
        menu.addEventListener(EventType.SELECT, event -> {
            throw new UnsupportedOperationException();
        });
        Result result = menu.receiveEvent(new Event(EventType.SELECT));
        Assertions.assertTrue(result.hasError());
    }

    @Test
    void testReceiveEvent_givenEventListenerExists_thenResultHasNoError() {
        Menu menu = new Menu("test");
        menu.addEventListener(EventType.SELECT, event -> {});
        Result result = menu.receiveEvent(new Event(EventType.SELECT));
        Assertions.assertFalse(result.hasError());
    }

    @Test
    void testReceiveEvent_givenEventListenerChangesOutsideScope_thenChangeOccurs() {
        AtomicBoolean executed = new AtomicBoolean(false);
        Menu menu = new Menu("test");
        menu.addEventListener(EventType.SELECT, event -> executed.set(true));
        Result result = menu.receiveEvent(new Event(EventType.SELECT));
        Assertions.assertFalse(result.hasError());
        Assertions.assertTrue(executed.get());
    }

    @Test
    void testReceiveEvent_givenEventListenerChangesContext_thenContextUpdated() {
        Menu menu = new Menu("test");
        Menu child = new Menu("child");
        menu.addChild(child);
        menu.addEventListener(EventType.SELECT, event -> Context.getInstance().push(child));
        Result result = menu.receiveEvent(new Event(EventType.SELECT));
        Assertions.assertFalse(result.hasError());
        Assertions.assertEquals(child, Context.getInstance().peek());
    }

}
