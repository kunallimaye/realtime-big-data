
package com.redhat.example;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "RealTimeBigDataService", targetNamespace = "http://example.redhat.com/", wsdlLocation = "http://10.64.193.144:8080/realtime-big-data-business/RealTimeBigData?wsdl")
public class RealTimeBigDataService
    extends Service
{

    private final static URL REALTIMEBIGDATASERVICE_WSDL_LOCATION;
    private final static WebServiceException REALTIMEBIGDATASERVICE_EXCEPTION;
    private final static QName REALTIMEBIGDATASERVICE_QNAME = new QName("http://example.redhat.com/", "RealTimeBigDataService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://10.64.193.144:8080/realtime-big-data-business/RealTimeBigData?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        REALTIMEBIGDATASERVICE_WSDL_LOCATION = url;
        REALTIMEBIGDATASERVICE_EXCEPTION = e;
    }

    public RealTimeBigDataService() {
        super(__getWsdlLocation(), REALTIMEBIGDATASERVICE_QNAME);
    }

    public RealTimeBigDataService(WebServiceFeature... features) {
        super(__getWsdlLocation(), REALTIMEBIGDATASERVICE_QNAME, features);
    }

    public RealTimeBigDataService(URL wsdlLocation) {
        super(wsdlLocation, REALTIMEBIGDATASERVICE_QNAME);
    }

    public RealTimeBigDataService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, REALTIMEBIGDATASERVICE_QNAME, features);
    }

    public RealTimeBigDataService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RealTimeBigDataService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns RealTimeBigData
     */
    @WebEndpoint(name = "RealTimeBigDataPort")
    public RealTimeBigData getRealTimeBigDataPort() {
        return super.getPort(new QName("http://example.redhat.com/", "RealTimeBigDataPort"), RealTimeBigData.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RealTimeBigData
     */
    @WebEndpoint(name = "RealTimeBigDataPort")
    public RealTimeBigData getRealTimeBigDataPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://example.redhat.com/", "RealTimeBigDataPort"), RealTimeBigData.class, features);
    }

    private static URL __getWsdlLocation() {
        if (REALTIMEBIGDATASERVICE_EXCEPTION!= null) {
            throw REALTIMEBIGDATASERVICE_EXCEPTION;
        }
        return REALTIMEBIGDATASERVICE_WSDL_LOCATION;
    }

}
