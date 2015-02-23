
package com.redhat.example;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "RealTimeBigData", targetNamespace = "http://example.redhat.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface RealTimeBigData {


    /**
     * 
     * @param information
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "execTime", targetNamespace = "")
    @RequestWrapper(localName = "entry", targetNamespace = "http://example.redhat.com/", className = "com.redhat.example.Entry")
    @ResponseWrapper(localName = "entryResponse", targetNamespace = "http://example.redhat.com/", className = "com.redhat.example.EntryResponse")
    public String entry(
        @WebParam(name = "information", targetNamespace = "")
        InformationEntry information);

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "execTime", targetNamespace = "")
    @RequestWrapper(localName = "execRegionCount", targetNamespace = "http://example.redhat.com/", className = "com.redhat.example.ExecRegionCount")
    @ResponseWrapper(localName = "execRegionCountResponse", targetNamespace = "http://example.redhat.com/", className = "com.redhat.example.ExecRegionCountResponse")
    public String execRegionCount();

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "execTime", targetNamespace = "")
    @RequestWrapper(localName = "execWordCount", targetNamespace = "http://example.redhat.com/", className = "com.redhat.example.ExecWordCount")
    @ResponseWrapper(localName = "execWordCountResponse", targetNamespace = "http://example.redhat.com/", className = "com.redhat.example.ExecWordCountResponse")
    public String execWordCount();

    /**
     * 
     * @param fileName
     * @param wait
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "execTime", targetNamespace = "")
    @RequestWrapper(localName = "entryFromLocalFile", targetNamespace = "http://example.redhat.com/", className = "com.redhat.example.EntryFromLocalFile")
    @ResponseWrapper(localName = "entryFromLocalFileResponse", targetNamespace = "http://example.redhat.com/", className = "com.redhat.example.EntryFromLocalFileResponse")
    public String entryFromLocalFile(
        @WebParam(name = "fileName", targetNamespace = "")
        String fileName,
        @WebParam(name = "wait", targetNamespace = "")
        long wait);

    /**
     * 
     * @param reducerScript
     * @param mapperScript
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "execTime", targetNamespace = "")
    @RequestWrapper(localName = "execScript", targetNamespace = "http://example.redhat.com/", className = "com.redhat.example.ExecScript")
    @ResponseWrapper(localName = "execScriptResponse", targetNamespace = "http://example.redhat.com/", className = "com.redhat.example.ExecScriptResponse")
    public String execScript(
        @WebParam(name = "mapperScript", targetNamespace = "")
        String mapperScript,
        @WebParam(name = "reducerScript", targetNamespace = "")
        String reducerScript);

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "execTime", targetNamespace = "")
    @RequestWrapper(localName = "execKeywordScoring", targetNamespace = "http://example.redhat.com/", className = "com.redhat.example.ExecKeywordScoring")
    @ResponseWrapper(localName = "execKeywordScoringResponse", targetNamespace = "http://example.redhat.com/", className = "com.redhat.example.ExecKeywordScoringResponse")
    public String execKeywordScoring();

}
