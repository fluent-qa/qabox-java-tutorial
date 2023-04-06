package io.fluent.qabox.processor;



import io.fluent.qabox.annotation.DataProcessor;
import io.fluent.qabox.config.constant.BoxConst;
import io.fluent.qabox.service.IBoxDataService;
import io.fluent.qabox.util.di.IocUtil;

import java.util.HashMap;
import java.util.Map;

public class DataProcessorManager {

    private static final Map<String, Class<? extends IBoxDataService>> dataServiceMap = new HashMap<>();

    public static void register(String name, Class<? extends IBoxDataService> eruptDataService) {
        dataServiceMap.put(name, eruptDataService);
    }

    public static IBoxDataService getDataProcessor(Class<?> clazz) {
        DataProcessor eruptDataProcessor = clazz.getAnnotation(DataProcessor.class);
        return IocUtil.getBean(dataServiceMap.get(null == eruptDataProcessor ?
                BoxConst.DEFAULT_DATA_PROCESSOR : eruptDataProcessor.value()));
    }
}
