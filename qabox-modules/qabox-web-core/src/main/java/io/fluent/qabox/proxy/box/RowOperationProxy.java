package io.fluent.qabox.proxy.box;

import io.fluent.qabox.Box;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.frontend.operation.RowOperation;
import io.fluent.qabox.proxy.AnnotationProxy;
import org.aopalliance.intercept.MethodInvocation;

public class RowOperationProxy extends AnnotationProxy<RowOperation, Box> {

  @Override
  protected Object invocation(MethodInvocation invocation) {
    if ("code".equals(invocation.getMethod().getName())) {
      if (AnnotationConst.EMPTY_STR.equals(this.rawAnnotation.code())) {
        return Integer.toString(this.rawAnnotation.title().hashCode());
      }
    }
    return this.invoke(invocation);
  }
}
