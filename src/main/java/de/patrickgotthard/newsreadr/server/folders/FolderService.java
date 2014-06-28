package de.patrickgotthard.newsreadr.server.folders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.patrickgotthard.newsreadr.server.common.exception.ServiceException;
import de.patrickgotthard.newsreadr.server.feeds.FeedService;
import de.patrickgotthard.newsreadr.server.security.SecurityService;
import de.patrickgotthard.newsreadr.server.users.User;
import de.patrickgotthard.newsreadr.shared.request.AddFolderRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveFolderRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateFolderRequest;
import de.patrickgotthard.newsreadr.shared.response.Response;

@Service
public class FolderService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedService.class);

    private final FolderRepository folderRepository;
    private final FeedService feedService;
    private final SecurityService securityService;

    @Autowired
    public FolderService(final FolderRepository folderRepository, final FeedService feedService, final SecurityService securityService) {
        this.folderRepository = folderRepository;
        this.feedService = feedService;
        this.securityService = securityService;
    }

    @Transactional
    public Response addFolder(final AddFolderRequest request) {

        LOG.debug("Adding folder: {}", request);

        final User currentUser = securityService.getCurrentUser();
        final String title = request.getTitle();

        final boolean folderAlreadyExists = folderRepository.countByUserAndTitle(currentUser, title) > 0;
        if (folderAlreadyExists) {
            throw ServiceException.withMessage("Folder '{}' already exists", title);
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
            throw ServiceException.withMessage("Folder could not be found");
        }

        if (securityService.notBelongsToUser(folder)) {
            throw ServiceException.withMessage("The requested folder does not belong to the current user");
        }

        final User currentUser = securityService.getCurrentUser();
        final String newTitle = request.getTitle();

        final boolean folderAlreadyExists = folderRepository.countByUserAndTitle(currentUser, newTitle) > 0;
        if (folderAlreadyExists) {
            throw ServiceException.withMessage("Folder '{}' already exists", newTitle);
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
            throw ServiceException.withMessage("Folder could not be found");
        }

        if (securityService.notBelongsToUser(folder)) {
            throw ServiceException.withMessage("The requested folder does not belong to the current user");
        }

        folderRepository.delete(folder);

        feedService.removeFeedsWithoutSubscribers();

        LOG.debug("Successfully deleted folder: {}", request);
        return Response.success();

    }

}