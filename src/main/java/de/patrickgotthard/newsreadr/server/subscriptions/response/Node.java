package de.patrickgotthard.newsreadr.server.subscriptions.response;

import java.util.List;

public class Node {

    public enum Type {
        ALL, BOOKMARKS, FOLDER, SUBSCRIPTION
    }

    private Type type;
    private Long id;
    private String title;
    private Long unread;
    private List<Node> subscriptions;
    private Long folderId;
    private String url;

    public Node() {
    }

    private Node(final Builder builder) {
        this.type = builder.type;
        this.id = builder.id;
        this.title = builder.title;
        this.unread = builder.unread;
        this.subscriptions = builder.subscriptions;
        this.folderId = builder.folderId;
        this.url = builder.url;
    }

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

    public Long getUnread() {
        return this.unread;
    }

    public void setUnread(final Long unread) {
        this.unread = unread;
    }

    public List<Node> getSubscriptions() {
        return this.subscriptions;
    }

    public void setSubscriptions(final List<Node> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Long getFolderId() {
        return this.folderId;
    }

    public void setFolderId(final Long folderId) {
        this.folderId = folderId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public static class Builder {

        private Type type;
        private Long id;
        private String title;
        private Long unread;
        private List<Node> subscriptions;
        private Long folderId;
        private String url;

        public Builder type(final Type type) {
            this.type = type;
            return this;
        }

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder unread(final Long unread) {
            this.unread = unread;
            return this;
        }

        public Builder subscriptions(final List<Node> subscriptions) {
            this.subscriptions = subscriptions;
            return this;
        }

        public Builder folderId(final Long folderId) {
            this.folderId = folderId;
            return this;
        }

        public Builder url(final String url) {
            this.url = url;
            return this;
        }

        public Node build() {
            return new Node(this);
        }

    }

}
