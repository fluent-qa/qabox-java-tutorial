package io.fluent.qabox.supplement.xml;

import cn.hutool.core.util.XmlUtil;
import io.fluent.qabox.supplement.xml.freemind.model.Map;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class XmlHelperTest {
  String xmlFilePath = "./demo.xml";

  @Test
  void getDocument() {
    Document doc = XmlHelper.getDocument(xmlFilePath);
    assertNotNull(doc);
  }

  @Test
  void getValueByXpath() {
    var result = XmlHelper.getValueByXpath(xmlFilePath, "//returnsms/message");
    assertEquals(result, "ok");
  }

  @Test
  void readXmlToObject() throws IOException {

  }

  @Test
  void writeBeanToFile() {
    XmlUtil.writeObjectAsXml(new File("temp.xml"), new DemoXmlBean());
  }

  @Data
  public static class DemoXmlBean {
    private String name = "name";
    private String value = "value";
  }

  @Test
  public void jAXBContextRead() throws Exception {
//    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//    URL schemaURL = Map.class.getClassLoader().getResource("schema/freemind.xsd");
//    Schema schema = factory.newSchema(schemaURL);
    var map = (Map) XmlHelper.readXmlToObject("./demo.mm", Map.class);
    assertNotNull(map.getNode());

  }
}