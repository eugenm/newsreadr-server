package de.patrickgotthard.newsreadr.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import de.patrickgotthard.newsreadr.shared.response.GetInfosResponse;

@Service
public class InfoService {

    private final Environment env;
    private final SecurityService securityService;

    @Autowired
    public InfoService(final Environment env, final SecurityService securityService) {
        this.env = env;
        this.securityService = securityService;
    }

    public GetInfosResponse getInfos() {
        final String serverVersion = env.getProperty("server.version");
        final String apiVersion = env.getProperty("api.version");
        final long userId = securityService.getCurrentUserId();
        final String username = securityService.getCurrentUsername();
        return new GetInfosResponse(serverVersion, apiVersion, userId, username);
    }
}
