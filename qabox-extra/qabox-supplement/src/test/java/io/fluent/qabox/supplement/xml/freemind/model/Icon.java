//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2022.09.23 时间 08:46:06 PM CST
//


package io.fluent.qabox.supplement.xml.freemind.model;

import jakarta.xml.bind.annotation.*;


/**
 * <p>anonymous complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="BUILTIN" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "icon")
public class Icon {

    @XmlAttribute(name = "BUILTIN", required = true)
    protected String builtin;

    /**
     * 获取builtin属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBUILTIN() {
        return builtin;
    }

    /**
     * 设置builtin属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBUILTIN(String value) {
        this.builtin = value;
    }

}
