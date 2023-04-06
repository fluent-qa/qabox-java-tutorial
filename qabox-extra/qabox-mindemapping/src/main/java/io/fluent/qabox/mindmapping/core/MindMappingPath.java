package io.fluent.qabox.mindmapping.core;



import java.util.LinkedList;


public interface MindMappingPath<T> {
  public T getRoot();

  public LinkedList<T> getChildrenNodes();
}
