package com.ethpalser.cli;

import com.ethpalser.cli.console.ConsoleReader;
import com.ethpalser.cli.console.ConsoleRunner;
import com.ethpalser.cli.console.ConsoleWriter;
import com.ethpalser.cli.menu.Context;
import com.ethpalser.cli.menu.Menu;
import com.ethpalser.cli.menu.SimpleMenu;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main {

    public static void main(String[] args) {
        Menu main = new SimpleMenu("main");
        main.addChild(new Menu("Resume"));

        Menu sub = new SimpleMenu("Start");
        sub.addChild(new Menu("Basic"));
        sub.addChild(new Menu("Advanced"));
        sub.addChild(new Menu("Custom"));

        main.addChild(sub);
        main.addChild(new Menu("Exit"));
        ConsoleRunner menu = new ConsoleRunner(Context.getInstance(), main);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        ConsoleWriter writer = new ConsoleWriter(bw);
        ConsoleReader reader = new ConsoleReader(new BufferedReader(new InputStreamReader(System.in)), bw);
        menu.open(reader, writer);
    }

}
