package io.fluent.qabox.mindmapping.freemind;

import io.fluent.qabox.mindmapping.freemind.model.Map;
import io.fluent.qabox.mindmapping.freemind.model.Node;
import io.fluent.qabox.supplement.xml.XmlHelper;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Freemind Mindmapping Accessor:
 * putting every method here to manipulation mindmapping
 */

public class FreemindAccessor {
  private Map root;
  private Document xmlStr;
  private final String freemindFilePath;
  private List<FreeMindPath> allPaths = new ArrayList<>();

  public FreemindAccessor(String freemindFilePath) {
    this.freemindFilePath = freemindFilePath;
    readFreemindFile(freemindFilePath);
    this.xmlStr = XmlHelper.getDocument(this.freemindFilePath);
  }


  private FreemindAccessor readFreemindFile(String filePath) {

    this.root = XmlHelper.readXmlToObject(filePath, Map.class);
    return this;
  }


  public Map getRoot() {
    return root;
  }

  /**
   * Generate MindMap Tree into a list of @MindmapPath Entity includes all the node path
   * from root to leaf
   *
   * @return
   */
  public FreemindAccessor generateAllPaths() {
    FreeMindPath rootPath = new FreeMindPath(this.root.getNode());
    List<FreeMindPath> nextLevel = populateNext(rootPath);
    populateAll(nextLevel);
    return this;
  }

  /**
   * populate current node's child nodes into the mindmap path
   *
   * @param current
   * @return
   */
  public List<FreeMindPath> populateNext(FreeMindPath current) {
    Node lastNode = current.getNodes().getLast();
    List<FreeMindPath> mms = new ArrayList<>();
    if (lastNode.getArrowlinkOrAttributeOrAttributeLayout().size() == 0) {
      mms = List.of(current);
    } else {
      for (Object o : lastNode.getArrowlinkOrAttributeOrAttributeLayout()) {
        FreeMindPath path = copyMindmapPath(current);
        if (o instanceof Node) {
          path.getNodes().add((Node) o);
        }
        mms.add(path);
      }
    }
    return mms;
  }

  /**
   * Deep Copy Current Path
   *
   * @param current
   * @return
   */
  private FreeMindPath copyMindmapPath(FreeMindPath current) {
    FreeMindPath path = new FreeMindPath(current.getRoot());
    path.setNodes(new LinkedList<>());
    for (Node node : current.getNodes()) {
      path.getNodes().add(node);
    }
    return path;
  }

  /**
   * populate all mindmapping path
   * 1. if the latest node has no child node, add to collection to go out of the recursive call
   * 2. if the latest node has child nodes, populate recursively until the latest node has no child node
   *
   * @param paths
   */
  public void populateAll(List<FreeMindPath> paths) {
    for (FreeMindPath mindmapPath : paths) {
      if (mindmapPath.getNodes().getLast().getArrowlinkOrAttributeOrAttributeLayout().size() == 0) {
        this.allPaths.add(mindmapPath);
      } else {
        populateAll(populateNext(mindmapPath));
      }
    }
  }

  /**
   * Get ALl the Mindmapping Path
   *
   * @return
   */
  public List<FreeMindPath> getAllPaths() {
    return allPaths;
  }
}
