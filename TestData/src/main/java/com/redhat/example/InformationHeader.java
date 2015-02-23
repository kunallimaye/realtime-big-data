
package com.redhat.example;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for informationHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="informationHeader">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="businessDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="region1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="region2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="region3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="source1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="source2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="systemDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "informationHeader", propOrder = {
    "businessDate",
    "key",
    "region1",
    "region2",
    "region3",
    "source1",
    "source2",
    "systemDate"
})
public class InformationHeader {

    protected String businessDate;
    protected String key;
    protected String region1;
    protected String region2;
    protected String region3;
    protected String source1;
    protected String source2;
    protected String systemDate;

    /**
     * Gets the value of the businessDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessDate() {
        return businessDate;
    }

    /**
     * Sets the value of the businessDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessDate(String value) {
        this.businessDate = value;
    }

    /**
     * Gets the value of the key property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * Gets the value of the region1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegion1() {
        return region1;
    }

    /**
     * Sets the value of the region1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegion1(String value) {
        this.region1 = value;
    }

    /**
     * Gets the value of the region2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegion2() {
        return region2;
    }

    /**
     * Sets the value of the region2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegion2(String value) {
        this.region2 = value;
    }

    /**
     * Gets the value of the region3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegion3() {
        return region3;
    }

    /**
     * Sets the value of the region3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegion3(String value) {
        this.region3 = value;
    }

    /**
     * Gets the value of the source1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSource1() {
        return source1;
    }

    /**
     * Sets the value of the source1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSource1(String value) {
        this.source1 = value;
    }

    /**
     * Gets the value of the source2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSource2() {
        return source2;
    }

    /**
     * Sets the value of the source2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSource2(String value) {
        this.source2 = value;
    }

    /**
     * Gets the value of the systemDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemDate() {
        return systemDate;
    }

    /**
     * Sets the value of the systemDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemDate(String value) {
        this.systemDate = value;
    }

}
