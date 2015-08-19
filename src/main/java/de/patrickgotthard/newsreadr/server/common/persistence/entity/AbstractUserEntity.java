package de.patrickgotthard.newsreadr.server.common.persistence.entity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractUserEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return this.user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

}
