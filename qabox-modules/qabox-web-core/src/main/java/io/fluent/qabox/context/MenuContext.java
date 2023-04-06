package io.fluent.qabox.context;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuContext {

    private String name;

    private String menuValue; //当前菜单值

    public MenuContext(String name) {
        this.name = name;
    }

    public MenuContext(String name, String menuValue) {
        this.name = name;
        this.menuValue = menuValue;
    }

    public MenuContext() {
    }
}