package de.patrickgotthard.newsreadr.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.patrickgotthard.newsreadr.shared.response.GetInfosResponse;

@Service
public class InfoService {

    private final SecurityService securityService;

    @Autowired
    public InfoService(final SecurityService securityService) {
        this.securityService = securityService;
    }

    public GetInfosResponse getInfos() {
        final String serverVersion = "1.0.0-SNAPSHOT"; // TODO
        final String apiVersion = "1.0.0-SNAPSHOT"; // TODO
        final long userId = securityService.getCurrentUserId();
        final String username = securityService.getCurrentUsername();
        return new GetInfosResponse(serverVersion, apiVersion, userId, username);
    }
}
