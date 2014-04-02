package de.patrickgotthard.newsreadr.server.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.server.service.FolderService;
import de.patrickgotthard.newsreadr.shared.request.AddFolderRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveFolderRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateFolderRequest;
import de.patrickgotthard.newsreadr.shared.response.Response;

@RestController
@RequestMapping("/api")
public class FolderController {

    private final FolderService folderService;

    @Autowired
    public FolderController(final FolderService folderService) {
        this.folderService = folderService;
    }

    @RequestMapping(params = AddFolderRequest.METHOD)
    public Response addFolder(final AddFolderRequest request) {
        return folderService.addFolder(request);
    }

    @RequestMapping(params = UpdateFolderRequest.METHOD)
    public Response updateFolder(final UpdateFolderRequest request) {
        return folderService.updateFolder(request);
    }

    @RequestMapping(params = RemoveFolderRequest.METHOD)
    public Response removeFolder(final RemoveFolderRequest request) {
        return folderService.removeFolder(request);
    }

}