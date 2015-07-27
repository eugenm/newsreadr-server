package de.patrickgotthard.newsreadr.server.infos.response;

public class GetInfosResponse {

    private Long userId;
    private String username;

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

}
