package com.ethpalser.cli;

import com.ethpalser.cli.menu.Menu;
import com.ethpalser.cli.menu.SimpleMenu;
import com.ethpalser.cli.console.ConsoleRunner;

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
