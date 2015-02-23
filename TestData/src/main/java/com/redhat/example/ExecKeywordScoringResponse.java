
package com.redhat.example;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for execKeywordScoringResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="execKeywordScoringResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="execTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "execKeywordScoringResponse", propOrder = {
    "execTime"
})
public class ExecKeywordScoringResponse {

    protected String execTime;

    /**
     * Gets the value of the execTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExecTime() {
        return execTime;
    }

    /**
     * Sets the value of the execTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExecTime(String value) {
        this.execTime = value;
    }

}