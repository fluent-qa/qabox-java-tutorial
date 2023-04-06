package io.fluent.qabox.module;

import io.fluent.qabox.util.di.IocUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BoxModuleInvoke {

    private static final List<Class<? extends BoxModule>> ERUPT_MODULES = new ArrayList<>();

    public static void addEruptModule(Class<? extends BoxModule> eruptModule) {
        ERUPT_MODULES.add(eruptModule);
    }

    public static void invoke(Consumer<BoxModule> consumer) {
        ERUPT_MODULES.forEach(it -> consumer.accept(IocUtil.getBean(it)));
    }

    public static int moduleNum() {
        return ERUPT_MODULES.size();
    }

}
