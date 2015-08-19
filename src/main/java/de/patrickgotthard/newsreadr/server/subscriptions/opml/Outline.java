package de.patrickgotthard.newsreadr.server.subscriptions.opml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Outline {

    @XmlAttribute(name = "type")
    private String type;

    @XmlAttribute(name = "text")
    private String text;

    @XmlAttribute(name = "title")
    private String title;

    @XmlAttribute(name = "xmlUrl")
    private String xmlUrl;

    @XmlAttribute(name = "htmlUrl")
    private String htmlUrl;

    @XmlElement(name = "outline")
    private List<Outline> outlines;

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getText() {
        return this.text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getXmlUrl() {
        return this.xmlUrl;
    }

    public void setXmlUrl(final String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    public void setHtmlUrl(final String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public List<Outline> getOutlines() {
        return this.outlines;
    }

    public void setOutlines(final List<Outline> outlines) {
        this.outlines = outlines;
    }

}
