package com.ethpalser.cli.menu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestContext {

    @Test
    void testRefresh_givenNoMenuChange_thenFalse() {
        Context context = Context.getInstance();
        boolean hasRefresh = context.refresh();
        Assertions.assertFalse(hasRefresh);
    }

    @Test
    void testRefresh_givenMenuChange_thenTrue() {
        Context context = Context.getInstance();
        context.setMenu(new Menu("empty"));
        boolean hasRefresh = context.refresh();
        Assertions.assertTrue(hasRefresh);
    }


}
