package com.ethpalser.cli.menu;

public class Context {

    private static Context context;
    private Menu menu;
    private boolean updated;

    private Context() {
        this.updated = false;
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
        if (this.menu == menu) {
            return;
        }
        this.menu = menu;
        this.updated = true;
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
