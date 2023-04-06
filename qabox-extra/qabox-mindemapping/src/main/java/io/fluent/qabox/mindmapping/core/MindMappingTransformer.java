package io.fluent.qabox.mindmapping.core;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class MindMappingTransformer {

  public static <T> List<String> transfer(MindMappingPath<T> path) {
    List<String> result = new LinkedList<>();
    for (T childrenNode : path.getChildrenNodes()) {
      if (childrenNode instanceof String) {
        result.add((String) childrenNode);
      } else {
        try {
          result.add(ReflectUtil.getFieldValue(childrenNode, "text").toString());
        } catch (Exception e) {
          result.add(StrUtil.EMPTY);
        }
      }
    }
    return result;
  }

  public static <N, R> R transfer(MindMappingPath<N> path, R obj) {
    Field[] fields = obj.getClass().getDeclaredFields();
    for (Field field : fields) {
      MindMappingLevel order = field.getAnnotation(MindMappingLevel.class);
      int index = order.value();
      try {
        //TODO: handler different type value
        ReflectUtil.setFieldValue(obj, field, ReflectUtil.getFieldValue(path.getChildrenNodes().get(index), "text"));
      } catch (Exception e) {
        log.error("transfer failed,", e);
      }
    }
    return obj;
  }
}
