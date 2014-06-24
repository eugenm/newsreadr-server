package de.patrickgotthard.newsreadr.server.infos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.shared.request.GetInfosRequest;
import de.patrickgotthard.newsreadr.shared.response.GetInfosResponse;

@RestController
@RequestMapping("/api")
class InfoController {

    private final InfoService infoService;

    @Autowired
    InfoController(final InfoService infoService) {
        this.infoService = infoService;
    }

    @RequestMapping(params = GetInfosRequest.METHOD)
    GetInfosResponse getInfos() {
        return infoService.getInfos();
    }

}