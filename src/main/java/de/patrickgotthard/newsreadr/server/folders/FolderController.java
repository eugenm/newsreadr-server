package de.patrickgotthard.newsreadr.server.folders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.shared.request.AddFolderRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveFolderRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateFolderRequest;
import de.patrickgotthard.newsreadr.shared.response.Response;

@RestController
@RequestMapping("/api")
class FolderController {

    private final FolderService folderService;

    @Autowired
    FolderController(final FolderService folderService) {
        this.folderService = folderService;
    }

    @RequestMapping(params = AddFolderRequest.METHOD)
    Response addFolder(final AddFolderRequest request) {
        return folderService.addFolder(request);
    }

    @RequestMapping(params = UpdateFolderRequest.METHOD)
    Response updateFolder(final UpdateFolderRequest request) {
        return folderService.updateFolder(request);
    }

    @RequestMapping(params = RemoveFolderRequest.METHOD)
    Response removeFolder(final RemoveFolderRequest request) {
        return folderService.removeFolder(request);
    }

}