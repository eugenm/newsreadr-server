package de.patrickgotthard.newsreadr.server.subscriptions.opml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Opml {

    @XmlAttribute(name = "version")
    private String version;

    @XmlElement(name = "head")
    private Head head;

    @XmlElement(name = "body")
    private Body body;

    public String getVersion() {
        return this.version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public Head getHead() {
        return this.head;
    }

    public void setHead(final Head head) {
        this.head = head;
    }

    public Body getBody() {
        return this.body;
    }

    public void setBody(final Body body) {
        this.body = body;
    }

}
