package io.fluent.qabox.context;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMenuContext {

    private static final ThreadLocal<UserMenuContext> threadLocal = ThreadLocal.withInitial(() -> {
        UserMenuContext boxUserContext = new UserMenuContext();
        boxUserContext.setMenuContext(new MenuContext());
        boxUserContext.setMetaUser(new BoxUserContext());
        return boxUserContext;
    });

    private MenuContext menuContext;

    private BoxUserContext metaUser;

    private String token;

    private static UserMenuContext getContext() {
        return threadLocal.get();
    }

    public static MenuContext getMenuContext() {
        return getContext().menuContext;
    }

    public static BoxUserContext getUser() {
        return getContext().metaUser;
    }

    public static String getToken() {
        return getContext().token;
    }

    //注册erupt上下文
    public static void register(MenuContext metaErupt) {
        getContext().setMenuContext(metaErupt);
    }

    //注册用户上下文
    public static void register(BoxUserContext metaUser) {
        getContext().setMetaUser(metaUser);
    }

    public static void registerToken(String token) {
        getContext().setToken(token);
    }

    public static void remove() {
        threadLocal.remove();
    }

}
