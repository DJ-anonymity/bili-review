package com.zfg.learn.bo;

public class Menu {

    private String menu_name;
    private String icon;

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menu_name='" + menu_name + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
