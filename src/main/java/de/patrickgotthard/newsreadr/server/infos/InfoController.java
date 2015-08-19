package de.patrickgotthard.newsreadr.server.infos;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.infos.response.GetInfosResponse;

@RestController
@RequestMapping("/api/infos")
class InfoController {

    @RequestMapping(method = RequestMethod.GET)
    public GetInfosResponse getInfos(final User currentUser) {

        final long userId = currentUser.getId();
        final String username = currentUser.getUsername();

        final GetInfosResponse response = new GetInfosResponse();
        response.setUserId(userId);
        response.setUsername(username);
        return response;

    }

}