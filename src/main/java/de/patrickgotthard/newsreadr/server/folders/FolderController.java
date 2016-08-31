package de.patrickgotthard.newsreadr.server.folders;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.server.folders.request.AddFolderRequest;
import de.patrickgotthard.newsreadr.server.folders.request.RemoveFolderRequest;
import de.patrickgotthard.newsreadr.server.folders.request.UpdateFolderRequest;

@RestController
@RequestMapping("/api/folders")
class FolderController {

    private final FolderService folderService;

    @Autowired
    public FolderController(final FolderService folderService) {
        this.folderService = folderService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addFolder(@Valid final AddFolderRequest request, final long currentUserId) {
        this.folderService.addFolder(request, currentUserId);
    }

    @RequestMapping(value = "/{folderId}", method = RequestMethod.PUT)
    public void updateFolder(@Valid final UpdateFolderRequest request, final long currentUserId) {
        this.folderService.updateFolder(request, currentUserId);
    }

    @RequestMapping(value = "/{folderId}", method = RequestMethod.DELETE)
    public void removeFolder(@Valid final RemoveFolderRequest request, final long currentUserId) {
        this.folderService.removeFolder(request, currentUserId);
    }

}
