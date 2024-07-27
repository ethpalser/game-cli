package com.ethpalser.cli.menu;

import java.util.ArrayDeque;
import java.util.Deque;

public class Context {

    private static Context context;

    private final Deque<Menu> menus;
    private boolean updated;

    private Context() {
        this.menus = new ArrayDeque<>();
        this.updated = false;
    }

    public static Context getInstance() {
        if (context == null) {
            context = new Context();
        }
        return context;
    }

    public Menu peek() {
        return this.menus.peek();
    }

    /**
     * Adds a new Menu to its stack. This becomes the new active Menu. Its updated status is set to true for
     * handling.
     *
     * @param next
     */
    public void push(Menu next) {
        if (this.peek() == next) {
            return;
        }
        this.menus.push(next);
        this.updated = true;
    }

    /**
     * Sets the active menu to the previous menu and returns the once active menu back. This operation is effectively
     * delete, and the returned result is only provided to handle this later. Its updated status set to true for
     * handling.
     *
     * @return Menu which has been removed from the stack
     */
    public Menu pop() {
        if (this.menus.peek() == null) {
            return null;
        }
        this.updated = true;
        return this.menus.pop();
    }

    /**
     * Sets the updated status to false and returns its previous status before being updated. <br/>
     * It is not recommended using this method solely to set its updated status to false. <br/>
     * Best use of this method is to use it in a while loop to check when the state has changed.
     *
     * @return true if updated was true when this method is called, otherwise false
     */
    public boolean refresh() {
        boolean result = this.updated;
        this.updated = false;
        return result;
    }

}
