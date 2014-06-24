package de.patrickgotthard.newsreadr.server.bookmarks.opml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Body {

    @XmlElement(name = "outline")
    private List<Outline> outlines;

    public List<Outline> getOutlines() {
        return outlines;
    }

    public void setOutlines(final List<Outline> outlines) {
        this.outlines = outlines;
    }

}
