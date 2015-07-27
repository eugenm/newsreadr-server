package de.patrickgotthard.newsreadr.server.common.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_entries")
public class UserEntry extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private Entry entry;

    @Column(name = "marked_read")
    private Boolean read;

    @Column(name = "bookmarked")
    private Boolean bookmarked;

    public UserEntry() {
    }

    private UserEntry(final Builder builder) {
        this.setId(builder.id);
        this.subscription = builder.subscription;
        this.entry = builder.entry;
        this.read = builder.read;
        this.bookmarked = builder.bookmarked;
    }

    public Subscription getSubscription() {
        return this.subscription;
    }

    public void setSubscription(final Subscription subscription) {
        this.subscription = subscription;
    }

    public Entry getEntry() {
        return this.entry;
    }

    public void setEntry(final Entry entry) {
        this.entry = entry;
    }

    public Boolean getRead() {
        return this.read;
    }

    public void setRead(final Boolean read) {
        this.read = read;
    }

    public Boolean getBookmarked() {
        return this.bookmarked;
    }

    public void setBookmarked(final Boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public static class Builder {

        private Long id;
        private Subscription subscription;
        private Entry entry;
        private Boolean read;
        private Boolean bookmarked;

        public Builder setId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder setSubscription(final Subscription subscription) {
            this.subscription = subscription;
            return this;
        }

        public Builder setEntry(final Entry entry) {
            this.entry = entry;
            return this;
        }

        public Builder setRead(final Boolean read) {
            this.read = read;
            return this;
        }

        public Builder setBookmarked(final Boolean bookmarked) {
            this.bookmarked = bookmarked;
            return this;
        }

        public UserEntry build() {
            return new UserEntry(this);
        }

    }

}
