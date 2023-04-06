package io.fluent.qabox.mindmapping.freemind;

import io.fluent.qabox.mindmapping.freemind.model.Map;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FreemindAccessorTest {
  FreemindAccessor accessor = new FreemindAccessor("./template.mm");

  @Test
  public void testInitializeFreemindAccessor() {
    var accessor = new FreemindAccessor("./template.mm");
    Map root = accessor.getRoot();
    assertNotNull(root);
  }

  @Test
  public void testGenerateAllPath() {
    List<FreeMindPath> allPaths = accessor.generateAllPaths().getAllPaths();
    assertEquals(allPaths.size(),4);
  }

  @Test
  public void testPopulateNextLevel() {
    FreeMindPath origin = new FreeMindPath(this.accessor.getRoot().getNode());
    List<FreeMindPath> next = accessor.populateNext(origin);
    assertEquals(next.size(),1);
    List<FreeMindPath> l2 = accessor.populateNext(next.get(0));
    assertEquals(l2.size(),2);

  }
}