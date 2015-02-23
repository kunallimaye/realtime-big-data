
package com.redhat.example;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.redhat.example package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ExecRegionCount_QNAME = new QName("http://example.redhat.com/", "execRegionCount");
    private final static QName _EntryFromLocalFileResponse_QNAME = new QName("http://example.redhat.com/", "entryFromLocalFileResponse");
    private final static QName _Entry_QNAME = new QName("http://example.redhat.com/", "entry");
    private final static QName _ExecWordCount_QNAME = new QName("http://example.redhat.com/", "execWordCount");
    private final static QName _ExecRegionCountResponse_QNAME = new QName("http://example.redhat.com/", "execRegionCountResponse");
    private final static QName _EntryFromLocalFile_QNAME = new QName("http://example.redhat.com/", "entryFromLocalFile");
    private final static QName _ExecKeywordScoring_QNAME = new QName("http://example.redhat.com/", "execKeywordScoring");
    private final static QName _ExecKeywordScoringResponse_QNAME = new QName("http://example.redhat.com/", "execKeywordScoringResponse");
    private final static QName _EntryResponse_QNAME = new QName("http://example.redhat.com/", "entryResponse");
    private final static QName _ExecScriptResponse_QNAME = new QName("http://example.redhat.com/", "execScriptResponse");
    private final static QName _ExecWordCountResponse_QNAME = new QName("http://example.redhat.com/", "execWordCountResponse");
    private final static QName _ExecScript_QNAME = new QName("http://example.redhat.com/", "execScript");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.redhat.example
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExecWordCount }
     * 
     */
    public ExecWordCount createExecWordCount() {
        return new ExecWordCount();
    }

    /**
     * Create an instance of {@link ExecRegionCountResponse }
     * 
     */
    public ExecRegionCountResponse createExecRegionCountResponse() {
        return new ExecRegionCountResponse();
    }

    /**
     * Create an instance of {@link ExecKeywordScoringResponse }
     * 
     */
    public ExecKeywordScoringResponse createExecKeywordScoringResponse() {
        return new ExecKeywordScoringResponse();
    }

    /**
     * Create an instance of {@link ExecKeywordScoring }
     * 
     */
    public ExecKeywordScoring createExecKeywordScoring() {
        return new ExecKeywordScoring();
    }

    /**
     * Create an instance of {@link EntryFromLocalFile }
     * 
     */
    public EntryFromLocalFile createEntryFromLocalFile() {
        return new EntryFromLocalFile();
    }

    /**
     * Create an instance of {@link ExecRegionCount }
     * 
     */
    public ExecRegionCount createExecRegionCount() {
        return new ExecRegionCount();
    }

    /**
     * Create an instance of {@link EntryFromLocalFileResponse }
     * 
     */
    public EntryFromLocalFileResponse createEntryFromLocalFileResponse() {
        return new EntryFromLocalFileResponse();
    }

    /**
     * Create an instance of {@link Entry }
     * 
     */
    public Entry createEntry() {
        return new Entry();
    }

    /**
     * Create an instance of {@link ExecWordCountResponse }
     * 
     */
    public ExecWordCountResponse createExecWordCountResponse() {
        return new ExecWordCountResponse();
    }

    /**
     * Create an instance of {@link ExecScript }
     * 
     */
    public ExecScript createExecScript() {
        return new ExecScript();
    }

    /**
     * Create an instance of {@link EntryResponse }
     * 
     */
    public EntryResponse createEntryResponse() {
        return new EntryResponse();
    }

    /**
     * Create an instance of {@link ExecScriptResponse }
     * 
     */
    public ExecScriptResponse createExecScriptResponse() {
        return new ExecScriptResponse();
    }

    /**
     * Create an instance of {@link InformationHeader }
     * 
     */
    public InformationHeader createInformationHeader() {
        return new InformationHeader();
    }

    /**
     * Create an instance of {@link InformationEntry }
     * 
     */
    public InformationEntry createInformationEntry() {
        return new InformationEntry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecRegionCount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example.redhat.com/", name = "execRegionCount")
    public JAXBElement<ExecRegionCount> createExecRegionCount(ExecRegionCount value) {
        return new JAXBElement<ExecRegionCount>(_ExecRegionCount_QNAME, ExecRegionCount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntryFromLocalFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example.redhat.com/", name = "entryFromLocalFileResponse")
    public JAXBElement<EntryFromLocalFileResponse> createEntryFromLocalFileResponse(EntryFromLocalFileResponse value) {
        return new JAXBElement<EntryFromLocalFileResponse>(_EntryFromLocalFileResponse_QNAME, EntryFromLocalFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Entry }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example.redhat.com/", name = "entry")
    public JAXBElement<Entry> createEntry(Entry value) {
        return new JAXBElement<Entry>(_Entry_QNAME, Entry.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecWordCount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example.redhat.com/", name = "execWordCount")
    public JAXBElement<ExecWordCount> createExecWordCount(ExecWordCount value) {
        return new JAXBElement<ExecWordCount>(_ExecWordCount_QNAME, ExecWordCount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecRegionCountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example.redhat.com/", name = "execRegionCountResponse")
    public JAXBElement<ExecRegionCountResponse> createExecRegionCountResponse(ExecRegionCountResponse value) {
        return new JAXBElement<ExecRegionCountResponse>(_ExecRegionCountResponse_QNAME, ExecRegionCountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntryFromLocalFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example.redhat.com/", name = "entryFromLocalFile")
    public JAXBElement<EntryFromLocalFile> createEntryFromLocalFile(EntryFromLocalFile value) {
        return new JAXBElement<EntryFromLocalFile>(_EntryFromLocalFile_QNAME, EntryFromLocalFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecKeywordScoring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example.redhat.com/", name = "execKeywordScoring")
    public JAXBElement<ExecKeywordScoring> createExecKeywordScoring(ExecKeywordScoring value) {
        return new JAXBElement<ExecKeywordScoring>(_ExecKeywordScoring_QNAME, ExecKeywordScoring.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecKeywordScoringResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example.redhat.com/", name = "execKeywordScoringResponse")
    public JAXBElement<ExecKeywordScoringResponse> createExecKeywordScoringResponse(ExecKeywordScoringResponse value) {
        return new JAXBElement<ExecKeywordScoringResponse>(_ExecKeywordScoringResponse_QNAME, ExecKeywordScoringResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example.redhat.com/", name = "entryResponse")
    public JAXBElement<EntryResponse> createEntryResponse(EntryResponse value) {
        return new JAXBElement<EntryResponse>(_EntryResponse_QNAME, EntryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecScriptResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example.redhat.com/", name = "execScriptResponse")
    public JAXBElement<ExecScriptResponse> createExecScriptResponse(ExecScriptResponse value) {
        return new JAXBElement<ExecScriptResponse>(_ExecScriptResponse_QNAME, ExecScriptResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecWordCountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example.redhat.com/", name = "execWordCountResponse")
    public JAXBElement<ExecWordCountResponse> createExecWordCountResponse(ExecWordCountResponse value) {
        return new JAXBElement<ExecWordCountResponse>(_ExecWordCountResponse_QNAME, ExecWordCountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecScript }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example.redhat.com/", name = "execScript")
    public JAXBElement<ExecScript> createExecScript(ExecScript value) {
        return new JAXBElement<ExecScript>(_ExecScript_QNAME, ExecScript.class, null, value);
    }

}
