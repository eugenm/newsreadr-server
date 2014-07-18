package de.patrickgotthard.newsreadr.server.infos;

import org.springframework.beans.factory.annotation.Autowired;

import de.patrickgotthard.newsreadr.server.common.web.ApiController;
import de.patrickgotthard.newsreadr.server.common.web.ApiRequestMapping;
import de.patrickgotthard.newsreadr.shared.request.GetInfosRequest;
import de.patrickgotthard.newsreadr.shared.response.GetInfosResponse;

@ApiController
class InfoController {

    private final InfoService infoService;

    @Autowired
    InfoController(final InfoService infoService) {
        this.infoService = infoService;
    }

    @ApiRequestMapping(GetInfosRequest.class)
    GetInfosResponse getInfos() {
        return infoService.getInfos();
    }

}