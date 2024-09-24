package com.ethpalser.cli.console;

import com.ethpalser.cli.menu.Context;
import com.ethpalser.cli.menu.Menu;
import com.ethpalser.cli.menu.MenuItem;
import com.ethpalser.cli.menu.SimpleMenu;
import com.ethpalser.cli.menu.event.EventType;
import com.ethpalser.cli.menu.exception.InvalidContextException;
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

    // region ConsoleRunner.runCycle tests

    /**
     * This is not a true blackbox test as we need to know that runCycle uses a ConsoleReader once every cycle.
     * Based on this assertion ConsoleReader has been set up to change its output every time it is called.
     *
     * ConsoleReader's output each run:
     * 1. -1
     * 2. 1 (which is then replaced with the first option's name)
     * 3. test -flag text
     * 4. back
     * 5. exit
     */

    @Test
    void testRunCycle_givenInvalidOption_thenNoExecute() throws InvalidContextException, IOException {
        // Main has a listener on every event that updates the eventOccurredSet to have that event, if it is received
        Menu main = this.testMainMenu();
        ConsoleRunner runner = new ConsoleRunner(main);
        ConsoleReader reader = new MockConsoleReader();
        ConsoleWriter writer = new MockConsoleWriter();

        // Run through each cycle once, using the first command from ConsoleReader, "-1".
        boolean canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);

        Assertions.assertTrue(eventOccurredSet.contains(EventType.PRE_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.POST_RENDER));
        Assertions.assertFalse(eventOccurredSet.contains(EventType.SELECT));
    }

    @Test
    void testRunCycle_givenValidOption_thenExecute() throws InvalidContextException, IOException {
        // Main has a listener on every event that updates the eventOccurredSet to have that event, if it is received
        Menu main = this.testMainMenu();
        main.addChild(this.testAction()); // Valid as option "1"

        ConsoleRunner runner = new ConsoleRunner(main);
        ConsoleReader reader = new MockConsoleReader();
        ConsoleWriter writer = new MockConsoleWriter();

        boolean canRun;
        // The first read command is invalid
        canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);
        // The second read command is "1"
        canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);
        Assertions.assertTrue(eventOccurredSet.contains(EventType.SELECT));

        // Sanity checks
        Assertions.assertTrue(eventOccurredSet.contains(EventType.PRE_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.POST_RENDER));
    }

    @Test
    void testRunCycle_givenSubmenuOption_thenContextUpdatedToNext() throws InvalidContextException, IOException {
        // Main has a listener on every event that updates the eventOccurredSet to have that event, if it is received
        Menu main = this.testMainMenu();
        main.addChild(this.testAction()); // Valid as option "1"

        MenuItem submenu = this.testSubmenu();
        main.addChild(submenu); // Valid as option "test"

        ConsoleRunner runner = new ConsoleRunner(main);
        ConsoleReader reader = new MockConsoleReader();
        ConsoleWriter writer = new MockConsoleWriter();

        boolean canRun;
        // The first read command is invalid
        canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);
        // The second read command is "1"
        canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);
        // The third read command is "test -flag text", which should change the context
        canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);
        Assertions.assertEquals(submenu, Context.getInstance().peek());

        // Sanity checks
        Assertions.assertTrue(eventOccurredSet.contains(EventType.PRE_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.POST_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.SELECT));
    }

    @Test
    void testRunCycle_givenBackOption_thenContextUpdatedToPrevious() throws InvalidContextException, IOException {
        // Main has a listener on every event that updates the eventOccurredSet to have that event, if it is received
        Menu main = this.testMainMenu();
        main.addChild(this.testAction()); // Valid as option "1"

        MenuItem submenu = this.testSubmenu();
        main.addChild(submenu); // Valid as option "test"

        ConsoleRunner runner = new ConsoleRunner(main);
        ConsoleReader reader = new MockConsoleReader();
        ConsoleWriter writer = new MockConsoleWriter();

        boolean canRun;
        // The first read command is invalid
        canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);
        // The second read command is "1"
        canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);
        // The third read command is "test -flag text", which should change the context
        canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);
        // The fourth read command is "back", which should change the context back to main
        canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);
        Assertions.assertEquals(main, Context.getInstance().peek());

        // Sanity checks
        Assertions.assertTrue(eventOccurredSet.contains(EventType.PRE_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.POST_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.SELECT));
    }

    @Test
    void testRunCycle_givenExitOption_thenReturnsFalse() throws InvalidContextException, IOException {
        // Main has a listener on every event that updates the eventOccurredSet to have that event, if it is received
        Menu main = this.testMainMenu();
        main.addChild(this.testAction()); // Valid as option "1"

        MenuItem submenu = this.testSubmenu();
        main.addChild(submenu); // Valid as option "test"

        ConsoleRunner runner = new ConsoleRunner(main);
        ConsoleReader reader = new MockConsoleReader();
        ConsoleWriter writer = new MockConsoleWriter();

        boolean canRun;
        // The first read command is invalid
        canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);
        // The second read command is "1"
        canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);
        // The third read command is "test -flag text", which should change the context
        canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);
        // The fourth read command is "back", which should change the context back to main
        canRun = runner.runCycle(reader, writer);
        Assertions.assertTrue(canRun);
        // The fifth read command is "exit", which should return false
        canRun = runner.runCycle(reader, writer);
        Assertions.assertFalse(canRun); // What this test is checking

        // Sanity checks
        Assertions.assertTrue(eventOccurredSet.contains(EventType.PRE_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.POST_RENDER));
        Assertions.assertTrue(eventOccurredSet.contains(EventType.SELECT));
    }

    // endregion

    @Test
    void testOpen_givenClosedReader_thenClose() {
        Menu main = this.testMainMenu();
        main.addChild(this.testSubmenu()); // Valid as option "test"

        ConsoleRunner runner = new ConsoleRunner(main);
        runner.close();
        runner.open();

        Assertions.assertFalse(eventOccurredSet.contains(EventType.PRE_RENDER));
        Assertions.assertFalse(eventOccurredSet.contains(EventType.RENDER));
        Assertions.assertFalse(eventOccurredSet.contains(EventType.POST_RENDER));
        Assertions.assertFalse(eventOccurredSet.contains(EventType.SELECT));
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
        action.addEventListener(EventType.SELECT, event -> {
            eventOccurredSet.add(EventType.SELECT);
        });
        return action;
    }

    private MenuItem testSubmenu() {
        // Test updating context from selecting a MenuItem
        Menu submenu = new Menu("test");
        submenu.addEventListener(EventType.SELECT, event -> {
            eventOccurredSet.add(EventType.SELECT);
            Context.getInstance().push(submenu);
        });
        return submenu;
    }

}
