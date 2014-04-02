package de.patrickgotthard.newsreadr.server.service.data.opml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Outline {

    @XmlAttribute
    private String type;

    @XmlAttribute
    private String text;

    @XmlAttribute
    private String title;

    @XmlAttribute
    private String xmlUrl;

    @XmlAttribute
    private String htmlUrl;

    @XmlElement(name = "outline")
    private List<Outline> outlines;

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getXmlUrl() {
        return xmlUrl;
    }

    public void setXmlUrl(final String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(final String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public List<Outline> getOutlines() {
        return outlines;
    }

    public void setOutlines(final List<Outline> outlines) {
        this.outlines = outlines;
    }

}
