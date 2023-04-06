package io.fluent.qabox.mindmapping.freemind;

import io.fluent.qabox.mindmapping.core.MindMappingPath;
import io.fluent.qabox.mindmapping.freemind.model.Node;
import lombok.Data;

import java.util.LinkedList;

@Data
public class FreeMindPath implements Cloneable, MindMappingPath<Node> {
  private final Node root;
  private LinkedList<Node> nodes = new LinkedList<>();

  public FreeMindPath(Node root) {
    this.root = root;
    this.nodes.add(root);
  }

  /**
   * TODO: move deep copy here or check the default implementation works
   * COPY nodes in new LinedList
   *
   * @return
   * @throws CloneNotSupportedException
   */
  @Override
  protected FreeMindPath clone() throws CloneNotSupportedException {
    return (FreeMindPath) super.clone();
  }

  @Override
  public LinkedList<Node> getChildrenNodes() {
    return this.nodes;
  }
}
