package com.ethpalser.cli.menu;

import com.ethpalser.cli.util.StringUtils;

public class SimpleMenu extends Menu {

    public SimpleMenu(String name, MenuItem[] children) {
        super(name, children);
    }

    public SimpleMenu(String name) {
        this(name, new MenuItem[]{});
    }

    @Override
    public String getTextDisplay() {
        return this.buildTextDisplayFromChildren(this.getChildren().values().toArray(new MenuItem[0]));
    }

    private String buildTextDisplayFromChildren(MenuItem[] children) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < children.length; i++) {
            if (i != 0)
                sb.append("\n");
            sb.append(i).append(". ").append(StringUtils.capitalizeWord(children[i].getName()));
        }
        return sb.toString();
    }

}
