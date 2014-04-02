package de.patrickgotthard.newsreadr.server.persistence.entity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractUserEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public AbstractUserEntity() {
    }

    protected AbstractUserEntity(final User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

}
