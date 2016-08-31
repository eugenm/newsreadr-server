package de.patrickgotthard.newsreadr.server.infos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.server.infos.response.GetInfosResponse;

@RestController
@RequestMapping("/api/infos")
class InfoController {

    private final InfoService infoService;

    @Autowired
    public InfoController(final InfoService infoService) {
        this.infoService = infoService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public GetInfosResponse getInfos(final long currentUserId) {
        return this.infoService.getInfos(currentUserId);
    }

}
