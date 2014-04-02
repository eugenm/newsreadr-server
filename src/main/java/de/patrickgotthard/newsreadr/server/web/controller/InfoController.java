package de.patrickgotthard.newsreadr.server.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.server.service.InfoService;
import de.patrickgotthard.newsreadr.shared.request.GetInfosRequest;
import de.patrickgotthard.newsreadr.shared.response.GetInfosResponse;

@RestController
@RequestMapping("/api")
public class InfoController {

    private final InfoService infoService;

    @Autowired
    public InfoController(final InfoService infoService) {
        this.infoService = infoService;
    }

    @RequestMapping(params = GetInfosRequest.METHOD)
    public GetInfosResponse getInfos() {
        return infoService.getInfos();
    }

}