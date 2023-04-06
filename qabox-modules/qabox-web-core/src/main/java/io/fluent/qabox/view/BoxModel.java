package io.fluent.qabox.view;

import com.google.gson.JsonObject;
import io.fluent.qabox.Box;
import io.fluent.qabox.processor.invoker.DataProxyInvoke;
import io.fluent.qabox.proxy.AnnotationProxy;
import io.fluent.qabox.proxy.box.BoxProxyContext;
import io.fluent.qabox.proxy.box.BoxProxy;
import io.fluent.qabox.util.AnnotationUtil;
import io.fluent.qabox.util.CloneSupport;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
@Setter
public final class BoxModel implements Cloneable {

    private transient Class<?> clazz;

    private transient Box box;

    private transient AnnotationProxy<Box, Void> boxProxy = new BoxProxy();

    private transient Map<String, BoxFieldModel> eruptFieldMap;

    private String eruptName;

    private JsonObject eruptJson;

    private List<BoxFieldModel> eruptFieldModels;

    //默认查询条件
    private Map<String, Object> searchCondition;

    private boolean extraRow = false;

    public BoxModel(Class<?> clz) {
        this.clazz = clz;
        this.box = clz.getAnnotation(Box.class);
        this.box = boxProxy.newProxy(this.getBox(), null);
        this.eruptName = clz.getSimpleName();
        DataProxyInvoke.invoke(this, it -> {
            try {
                it.getClass().getDeclaredMethod("extraRow", List.class);
                this.extraRow = true;
            } catch (NoSuchMethodException ignored) {
            }
        });
    }

    public Box getBox() {
        BoxProxyContext.set(clazz);
        return box;
    }

    @Override
    public final BoxModel clone() throws CloneNotSupportedException {
      BoxModel boxModel = (BoxModel) super.clone();
      boxModel.eruptJson = AnnotationUtil.annotationToJsonByReflect(this.getBox());
      boxModel.eruptFieldModels = eruptFieldModels.stream().map(CloneSupport::clone)
                .peek(BoxFieldModel::serializable).collect(Collectors.toList());
        return boxModel;
    }

}
