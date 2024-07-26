package com.github.ethpalser;

import com.github.ethpalser.menu.Menu;
import com.github.ethpalser.menu.SimpleMenu;
import com.github.ethpalser.console.ConsoleRunner;

public class main {

    public static void main(String[] args) {
        Menu main = new SimpleMenu("main");
        main.addChild(new Menu("resume"));
        main.addChild(new Menu("start"));
        main.addChild(new Menu("exit"));

        ConsoleRunner menu = new ConsoleRunner(null);
        menu.open();
    }

}
