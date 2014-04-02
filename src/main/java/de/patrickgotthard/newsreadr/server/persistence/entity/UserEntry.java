package de.patrickgotthard.newsreadr.server.persistence.entity;

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

    public UserEntry(final Subscription subscription, final Entry entry, final Boolean read, final Boolean bookmarked) {
        this.subscription = subscription;
        this.entry = entry;
        this.read = read;
        this.bookmarked = bookmarked;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(final Subscription subscription) {
        this.subscription = subscription;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(final Entry entry) {
        this.entry = entry;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(final Boolean read) {
        this.read = read;
    }

    public Boolean getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(final Boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public static class Builder {

        private Subscription subscription;
        private Entry entry;
        private Boolean read;
        private Boolean bookmarked;

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
            return new UserEntry(subscription, entry, read, bookmarked);
        }

    }

}
