package io.fluent.qabox.processor.invoker;



import io.fluent.qabox.data.fun.DataProxy;
import io.fluent.qabox.data.fun.PreDataProxy;
import io.fluent.qabox.util.di.IocUtil;
import io.fluent.qabox.util.internal.ReflectUtil;
import io.fluent.qabox.view.BoxModel;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class DataProxyInvoke {

    public static void invoke(BoxModel boxModel, Consumer<DataProxy<Object>> consumer) {
        //父类及接口 @PreDataProxy
        ReflectUtil.findClassExtendStack(boxModel.getClazz()).forEach(clazz -> DataProxyInvoke.actionInvokePreDataProxy(clazz, consumer));
        //本类及接口 @PreDataProxy
        DataProxyInvoke.actionInvokePreDataProxy(boxModel.getClazz(), consumer);
        //@Erupt → DataProxy
        Stream.of(boxModel.getBox().dataProxy()).forEach(proxy -> consumer.accept(getInstanceBean(proxy)));
    }

    private static void actionInvokePreDataProxy(Class<?> clazz, Consumer<DataProxy<Object>> consumer) {
        //接口
        Stream.of(clazz.getInterfaces()).forEach(it -> Optional.ofNullable(it.getAnnotation(PreDataProxy.class))
                .ifPresent(dataProxy -> consumer.accept(getInstanceBean(dataProxy.value()))));
        //类
        Optional.ofNullable(clazz.getAnnotation(PreDataProxy.class))
                .ifPresent(dataProxy -> consumer.accept(getInstanceBean(dataProxy.value())));
    }

    private static DataProxy<Object> getInstanceBean(Class<? extends DataProxy<?>> dataProxy) {
        return (DataProxy) IocUtil.getBean(dataProxy);
    }

}
