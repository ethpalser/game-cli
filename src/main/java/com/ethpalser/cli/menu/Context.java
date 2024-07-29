package com.ethpalser.cli.menu;

import java.util.ArrayDeque;
import java.util.Deque;

public class Context {

    private static Context context;

    private final Deque<Menu> menus;
    private Menu defaultMenu;
    private boolean updated;

    private Context() {
        this.menus = new ArrayDeque<>();
        this.defaultMenu = null;
        this.updated = false;
    }

    public static Context getInstance() {
        if (context == null) {
            context = new Context();
        }
        return context;
    }

    /**
     * Returns the current menu on the stack or a default menu if this stack is empty.
     *
     * @return Menu
     */
    public Menu peek() {
        if (!this.menus.isEmpty()) {
            return this.menus.peek();
        }
        return this.defaultMenu;
    }

    /**
     * Adds a new Menu to its stack. This becomes the new active Menu. Its updated status is set to true for
     * handling.
     *
     * @param next Menu
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
        if (this.menus.isEmpty()) {
            return this.defaultMenu;
        }
        this.updated = true;
        return this.menus.pop();
    }

    /**
     * If the size of the menu stack is 0 it is considered empty. This can be used to determine if the ability to
     * pop the current menu is allowed. There can be a default menu and be empty, which in this case you are
     * not be allowed to pop. Since pop returns the default menu whenever it is empty this is not needed to wrap
     * around pop as a safeguard. Instead, this can be used to inform the user they cannot go further back in menus.
     *
     * @return true if there are no menus left to remove (pop), otherwise false
     */
    public boolean isEmpty() {
        return this.menus.isEmpty();
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

    /**
     * Sets the default menu to use whenever the menu stack is emptied. It is recommended to set the default menu,
     * such as a main menu, before or immediately upon using this context. It is up to the user to handle exceptions
     * if the default is not set.
     *
     * @param menu Menu
     */
    public void setDefault(Menu menu) {
        this.defaultMenu = menu;
    }

    /**
     * Returns the default menu, such as the main menu.
     *
     * @return Menu
     */
    public Menu getDefault() {
        return this.defaultMenu;
    }

}
