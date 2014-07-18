package de.patrickgotthard.newsreadr.server.folders;

import org.springframework.beans.factory.annotation.Autowired;

import de.patrickgotthard.newsreadr.server.common.web.ApiController;
import de.patrickgotthard.newsreadr.server.common.web.ApiRequestMapping;
import de.patrickgotthard.newsreadr.shared.request.AddFolderRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveFolderRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateFolderRequest;
import de.patrickgotthard.newsreadr.shared.response.Response;

@ApiController
class FolderController {

    private final FolderService folderService;

    @Autowired
    FolderController(final FolderService folderService) {
        this.folderService = folderService;
    }

    @ApiRequestMapping(AddFolderRequest.class)
    Response addFolder(final AddFolderRequest request) {
        return folderService.addFolder(request);
    }

    @ApiRequestMapping(UpdateFolderRequest.class)
    Response updateFolder(final UpdateFolderRequest request) {
        return folderService.updateFolder(request);
    }

    @ApiRequestMapping(RemoveFolderRequest.class)
    Response removeFolder(final RemoveFolderRequest request) {
        return folderService.removeFolder(request);
    }

}