package io.fluent.qabox.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import io.fluent.qabox.context.UserMenuContext;
import io.fluent.qabox.context.MenuContext;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.util.internal.ReflectUtil;
import lombok.SneakyThrows;

import java.lang.reflect.Field;


public class GsonExclusionStrategies implements ExclusionStrategy {

    @Override
    @SneakyThrows
    public boolean shouldSkipField(FieldAttributes f) {
        MenuContext menu = UserMenuContext.getMenuContext();
        if (null == menu || null == menu.getName()) return false;
        if (null == f.getAnnotation(SmartSkipSerialize.class)) return false;
        Field ff = ReflectUtil.findClassField(BoxCoreService.getBoxModel(menu.getName()).getClazz(), f.getName());
        if (null == ff) return false;
        return !f.getDeclaringClass().getName().equals(ff.getDeclaringClass().getName());
    }

    @Override
    public boolean shouldSkipClass(Class<?> incomingClass) {
        return false;
    }

}
