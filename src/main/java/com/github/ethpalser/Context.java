package com.github.ethpalser;

import com.github.ethpalser.menu.Menu;

public class Context {

    private static Context context;
    private Menu menu;

    private Context() {
    }

    public static Context getInstance() {
        if (context == null) {
            context = new Context();
        }
        return context;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

}
