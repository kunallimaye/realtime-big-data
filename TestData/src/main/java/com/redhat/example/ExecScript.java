
package com.redhat.example;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for execScript complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="execScript">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mapperScript" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reducerScript" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "execScript", propOrder = {
    "mapperScript",
    "reducerScript"
})
public class ExecScript {

    protected String mapperScript;
    protected String reducerScript;

    /**
     * Gets the value of the mapperScript property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMapperScript() {
        return mapperScript;
    }

    /**
     * Sets the value of the mapperScript property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMapperScript(String value) {
        this.mapperScript = value;
    }

    /**
     * Gets the value of the reducerScript property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReducerScript() {
        return reducerScript;
    }

    /**
     * Sets the value of the reducerScript property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReducerScript(String value) {
        this.reducerScript = value;
    }

}
