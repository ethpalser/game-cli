package com.ethpalser.cli;

import com.ethpalser.cli.console.ConsoleRunner;
import com.ethpalser.cli.menu.Menu;
import com.ethpalser.cli.menu.SimpleMenu;

public class Main {

    public static void main(String[] args) {
        Menu main = testMenu();
        ConsoleRunner menu = new ConsoleRunner(main);
        menu.open();
    }

    private static Menu testMenu() {
        Menu main = new SimpleMenu("main");
        main.addChild(new Menu("Resume"));

        Menu sub = new SimpleMenu("Start");
        sub.addChild(new Menu("Basic"));
        sub.addChild(new Menu("Advanced"));
        sub.addChild(new Menu("Custom"));

        main.addChild(sub);
        main.addChild(new Menu("Exit"));
        return main;
    }

}
