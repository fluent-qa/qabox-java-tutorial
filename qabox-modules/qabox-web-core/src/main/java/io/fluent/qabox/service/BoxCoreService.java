package io.fluent.qabox.service;

import io.fluent.qabox.Box;
import io.fluent.qabox.UIField;
import io.fluent.qabox.exception.BoxAnnotationException;
import io.fluent.qabox.frontend.field.Edit;
import io.fluent.qabox.frontend.field.EditType;
import io.fluent.qabox.module.BoxModuleInvoke;
import io.fluent.qabox.util.BoxUtil;
import io.fluent.qabox.util.TimeRecorder;
import io.fluent.qabox.util.di.IocUtil;
import io.fluent.qabox.util.internal.ReflectUtil;
import io.fluent.qabox.view.BoxFieldModel;
import io.fluent.qabox.view.BoxModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Order(100)
@Service
@Slf4j
public class BoxCoreService implements ApplicationRunner {

    private static final Map<String, BoxModel> boxModels = new LinkedCaseInsensitiveMap<>();

    private static final List<BoxModel> ERUPT_LIST = new ArrayList<>();

    private static final List<String> MODULES = new ArrayList<>();

    public static List<String> getModules() {
        return MODULES;
    }

    public static List<BoxModel> getBoxModels() {
        return ERUPT_LIST;
    }

    public static BoxModel getBoxModel(String eruptName) {
        return boxModels.get(eruptName);
    }

    //动态注册erupt类
    public static void registerErupt(Class<?> eruptClazz) {
        if (boxModels.containsKey(eruptClazz.getSimpleName())) {
            throw new RuntimeException(eruptClazz.getSimpleName() + " conflict !");
        }
        BoxModel BoxModel = initBoxModel(eruptClazz);
        boxModels.put(eruptClazz.getSimpleName(), BoxModel);
        ERUPT_LIST.add(BoxModel);
    }

    //移除容器内所维护的Erupt
    public static void unregisterErupt(Class<?> eruptClazz) {
        boxModels.remove(eruptClazz.getSimpleName());
        ERUPT_LIST.removeIf(model -> model.getEruptName().equals(eruptClazz.getSimpleName()));
    }

    @SneakyThrows
    public static BoxModel getEruptView(String eruptName) {
        BoxModel em = getBoxModel(eruptName).clone();
        for (BoxFieldModel fieldModel : em.getEruptFieldModels()) {
            Edit edit = fieldModel.getUiField().edit();
            if (edit.type() == EditType.CHOICE) {
                fieldModel.setChoiceList(BoxUtil.getChoiceList(edit.choiceType()));
            } else if (edit.type() == EditType.TAGS) {
                fieldModel.setTagList(BoxUtil.getTagList(edit.tagsType()));
            }
        }
        return em;
    }

    private static BoxModel initBoxModel(Class<?> clazz) {
        // erupt class data to memory
        BoxModel boxModel = new BoxModel(clazz);
        // erupt field data to memory
        boxModel.setEruptFieldModels(new ArrayList<>());
        boxModel.setEruptFieldMap(new LinkedCaseInsensitiveMap<>());
        ReflectUtil.findClassAllFields(clazz, field -> Optional.ofNullable(field.getAnnotation(UIField.class))
                .ifPresent(ignore -> {
                    BoxFieldModel eruptFieldModel = new BoxFieldModel(field);
                    boxModel.getEruptFieldModels().add(eruptFieldModel);
                    boxModel.getEruptFieldMap().put(field.getName(), eruptFieldModel);
                }));
        boxModel.getEruptFieldModels().sort(Comparator.comparingInt((a) -> a.getUiField().sort()));
        // erupt annotation validate
        BoxAnnotationException.validateBoxInfo(boxModel);
        return boxModel;
    }

    @Override
    public void run(ApplicationArguments args) {
        TimeRecorder totalRecorder = new TimeRecorder();
        TimeRecorder timeRecorder = new TimeRecorder();
        IocUtil.scannerPackage(BoxAppBeanService.getScanPackage(), new TypeFilter[]{
                new AnnotationTypeFilter(Box.class)
        }, clazz -> {
            BoxModel BoxModel = initBoxModel(clazz);
            boxModels.put(clazz.getSimpleName(), BoxModel);
            ERUPT_LIST.add(BoxModel);
        });
        log.info("<" + repeat("===", 20) + ">");
        AtomicInteger moduleMaxCharLength = new AtomicInteger();
        BoxModuleInvoke.invoke(it -> {
            int length = it.info().getName().length();
            if (length > moduleMaxCharLength.get()) {
                moduleMaxCharLength.set(length);
            }
        });
      BoxModuleInvoke.invoke(it -> {
            it.run();
            MODULES.add(it.info().getName());
            String moduleName = fillCharacter(it.info().getName(), moduleMaxCharLength.get());
            log.info("🚀 -> {} module initialization completed in {}ms", moduleName, timeRecorder.recorder());
        });
        log.info(" modules : " + MODULES.size());
        log.info(" classes : " + boxModels.size());
        log.info(" Framework initialization completed in {}ms", totalRecorder.recorder());
        log.info("<" + repeat("===", 20) + ">");
    }

    private String fillCharacter(String character, int targetWidth) {
        return character + repeat(" ", targetWidth - character.length());
    }

    private String repeat(String space, int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) sb.append(space);
        return sb.toString();
    }
}
