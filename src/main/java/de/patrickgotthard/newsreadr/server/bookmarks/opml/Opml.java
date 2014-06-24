package de.patrickgotthard.newsreadr.server.bookmarks.opml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Opml {

    @XmlAttribute
    private String version;

    @XmlElement
    private Head head;

    @XmlElement
    private Body body;

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(final Head head) {
        this.head = head;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(final Body body) {
        this.body = body;
    }

}
