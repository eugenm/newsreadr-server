package de.patrickgotthard.newsreadr.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.patrickgotthard.newsreadr.server.persistence.entity.Folder;
import de.patrickgotthard.newsreadr.server.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.persistence.repository.FolderRepository;
import de.patrickgotthard.newsreadr.server.service.data.ServiceException;
import de.patrickgotthard.newsreadr.shared.request.AddFolderRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveFolderRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateFolderRequest;
import de.patrickgotthard.newsreadr.shared.response.Response;

@Service
public class FolderService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedService.class);

    private final FolderRepository folderRepository;
    private final SharedService sharedService;
    private final SecurityService securityService;

    @Autowired
    public FolderService(final FolderRepository folderRepository, final SharedService sharedService, final SecurityService securityService) {
        this.folderRepository = folderRepository;
        this.sharedService = sharedService;
        this.securityService = securityService;
    }

    @Transactional
    public Response addFolder(final AddFolderRequest request) {

        LOG.debug("Adding folder: {}", request);

        final User currentUser = securityService.getCurrentUser();
        final String title = request.getTitle();

        final boolean folderAlreadyExists = folderRepository.countByUserAndTitle(currentUser, title) > 0;
        if (folderAlreadyExists) {
            throw new ServiceException("Folder '{}' already exists", title);
        }

        final Folder folder = new Folder.Builder().setUser(currentUser).setTitle(title).build();
        folderRepository.save(folder);

        LOG.debug("Successfully added folder: {}", request);
        return Response.success();

    }

    @Transactional
    public Response updateFolder(final UpdateFolderRequest request) {

        LOG.debug("Updating folder: {}", request);

        final long folderId = request.getFolderId();
        final Folder folder = folderRepository.findOne(folderId);

        if (folder == null) {
            throw new ServiceException("Folder could not be found");
        }

        if (securityService.notBelongsToUser(folder)) {
            throw new ServiceException("The requested folder does not belong to the current user");
        }

        final User currentUser = securityService.getCurrentUser();
        final String newTitle = request.getTitle();

        final boolean folderAlreadyExists = folderRepository.countByUserAndTitle(currentUser, newTitle) > 0;
        if (folderAlreadyExists) {
            throw new ServiceException("Folder '{}' already exists", newTitle);
        }

        folder.setTitle(newTitle);
        folderRepository.save(folder);

        LOG.debug("Successfully updated folder: {}", request);
        return Response.success();

    }

    @Transactional
    public Response removeFolder(final RemoveFolderRequest request) {

        LOG.debug("Deleting folder: {}", request);

        final long folderId = request.getFolderId();
        final Folder folder = folderRepository.findOne(folderId);

        if (folder == null) {
            throw new ServiceException("Folder could not be found");
        }

        if (securityService.notBelongsToUser(folder)) {
            throw new ServiceException("The requested folder does not belong to the current user");
        }

        folderRepository.delete(folder);

        sharedService.removeFeedsWithoutSubscribers();

        LOG.debug("Successfully deleted folder: {}", request);
        return Response.success();

    }

}
