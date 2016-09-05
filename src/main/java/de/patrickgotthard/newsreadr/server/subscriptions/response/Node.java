package de.patrickgotthard.newsreadr.server.subscriptions.response;

public class Node {

    public enum Type {
        ALL, BOOKMARKS, SUBSCRIPTION
    }

    private Type type;
    private Long id;
    private String title;
    private String url;
    private Long unread;

    public Type getType() {
        return this.type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Long getUnread() {
        return this.unread;
    }

    public void setUnread(final Long unread) {
        this.unread = unread;
    }

}
