package com.ethpalser.cli.console;

import com.ethpalser.cli.menu.Context;
import com.ethpalser.cli.menu.Menu;
import com.ethpalser.cli.menu.MenuItem;
import com.ethpalser.cli.menu.SimpleMenu;
import com.ethpalser.cli.menu.event.EventType;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestConsoleRunner {

    private Set<EventType> eventOccurredSet;

    @BeforeEach
    private void BeforeEach() {
        this.eventOccurredSet = new HashSet<>();
    }

    @Test
    void testOpen_givenInvalidOption_thenNoExecute() {
        Menu main = this.testMainMenu();
        ConsoleRunner runner = new ConsoleRunner(Context.getInstance(), main);
        runner.open(new MockConsoleReader(), new MockConsoleWriter());

        Assertions.assertTrue(eventOccurredSet.contains(EventType.PRE_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.POST_RENDER));
        Assertions.assertFalse(eventOccurredSet.contains(EventType.EXECUTE));
    }

    @Test
    void testOpen_givenValidOption_thenExecute() {
        Menu main = this.testMainMenu();
        main.addChild(this.testAction()); // Valid as option "1"
        ConsoleRunner runner = new ConsoleRunner(Context.getInstance(), main);
        runner.open(new MockConsoleReader(), new MockConsoleWriter());

        Assertions.assertTrue(eventOccurredSet.contains(EventType.PRE_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.POST_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.EXECUTE));
    }

    @Test
    void testOpen_givenSubmenuOption_thenContextUpdatedToNext() {
        Menu main = this.testMainMenu();
        MenuItem submenu = this.testSubmenu();
        main.addChild(submenu); // Valid as option "test"
        ConsoleRunner runner = new ConsoleRunner(Context.getInstance(), main);
        runner.open(new MockConsoleReader(), new MockConsoleWriter());

        Assertions.assertTrue(eventOccurredSet.contains(EventType.PRE_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.POST_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.EXECUTE));
        Assertions.assertEquals(submenu, Context.getInstance().peek());
    }

    @Test
    void testOpen_givenBackOption_thenContextUpdatedToPrevious() {
        Menu main = this.testMainMenu();
        main.addChild(this.testSubmenu()); // Valid as option "test"
        ConsoleRunner runner = new ConsoleRunner(Context.getInstance(), main);
        runner.open(new MockConsoleReader(), new MockConsoleWriter());

        Assertions.assertTrue(eventOccurredSet.contains(EventType.PRE_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.POST_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.EXECUTE));
        Assertions.assertEquals(main, Context.getInstance().peek());
    }

    @Test
    void testOpen_givenExitOption_thenClose() {
        Menu main = this.testMainMenu();
        main.addChild(this.testSubmenu()); // Valid as option "test"
        ConsoleRunner runner = new ConsoleRunner(Context.getInstance(), main);
        runner.open(new MockConsoleReader(), new MockConsoleWriter());

        Assertions.assertTrue(eventOccurredSet.contains(EventType.PRE_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.POST_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.EXECUTE));
    }

    @Test
    void testOpen_givenClosedReader_thenClose() {
        Menu main = this.testMainMenu();
        main.addChild(this.testSubmenu()); // Valid as option "test"
        ConsoleRunner runner = new ConsoleRunner(Context.getInstance(), main);
        ConsoleReader reader = new MockConsoleReader();
        try {
            reader.close();
        } catch (IOException e) {
            // This shouldn't happen as mock reader has no IO
            e.printStackTrace();
        }
        runner.open(new MockConsoleReader(), new MockConsoleWriter());

        Assertions.assertFalse(eventOccurredSet.contains(EventType.PRE_RENDER));
        Assertions.assertFalse(eventOccurredSet.contains(EventType.RENDER));
        Assertions.assertFalse(eventOccurredSet.contains(EventType.POST_RENDER));
        Assertions.assertFalse(eventOccurredSet.contains(EventType.EXECUTE));
    }

    private Menu testMainMenu() {
        // Test initial events being emitted
        Menu main = new SimpleMenu("main");
        for (EventType e : EventType.values()) {
            main.addEventListener(e, event -> eventOccurredSet.add(e));
        }
        return main;
    }

    private MenuItem testAction() {
        // Test execute event from selecting a MenuItem
        MenuItem action = new MenuItem("other");
        action.addEventListener(EventType.EXECUTE, event -> {
            eventOccurredSet.add(EventType.EXECUTE);
        });
        return action;
    }

    private MenuItem testSubmenu() {
        // Test updating context from selecting a MenuItem
        Menu submenu = new Menu("test");
        submenu.addEventListener(EventType.EXECUTE, event -> {
            eventOccurredSet.add(EventType.EXECUTE);
            Context.getInstance().push(submenu);
        });
        return submenu;
    }

}
