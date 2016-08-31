package de.patrickgotthard.newsreadr.server.folders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Folder;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QFolder;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.FolderRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserRepository;
import de.patrickgotthard.newsreadr.server.common.rest.AlreadyExistsException;
import de.patrickgotthard.newsreadr.server.common.rest.NotFoundException;
import de.patrickgotthard.newsreadr.server.feeds.FeedService;
import de.patrickgotthard.newsreadr.server.folders.request.AddFolderRequest;
import de.patrickgotthard.newsreadr.server.folders.request.RemoveFolderRequest;
import de.patrickgotthard.newsreadr.server.folders.request.UpdateFolderRequest;

@Service
class FolderService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedService.class);

    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final FeedService feedService;

    @Autowired
    public FolderService(final UserRepository userRepository, final FolderRepository folderRepository, final FeedService feedService) {
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
        this.feedService = feedService;
    }

    @Transactional
    public void addFolder(final AddFolderRequest request, final long currentUserId) {

        LOG.debug("Adding folder: {}", request);
        final String title = request.getTitle();

        final boolean folderExists = this.folderExists(title, currentUserId);
        if (folderExists) {
            throw new AlreadyExistsException("The folder " + title + " does already exist");
        }

        final User currentUser = this.userRepository.findOne(currentUserId);

        final Folder folder = new Folder();
        folder.setUser(currentUser);
        folder.setTitle(title);

        this.folderRepository.save(folder);

        LOG.debug("Successfully added folder: {}", title);

    }

    @Transactional
    public void updateFolder(final UpdateFolderRequest request, final long currentUserId) {

        LOG.debug("Updating folder: {}", request);

        // load folder
        final Folder folder = this.loadFolder(request.getFolderId(), currentUserId);
        if (folder == null) {
            throw new NotFoundException("Folder does not exist");
        }

        // check whether new folder name already exists
        final String newTitle = request.getTitle();

        final boolean folderAlreadyExists = this.folderExists(newTitle, currentUserId);
        if (folderAlreadyExists) {
            throw new AlreadyExistsException("The folder " + newTitle + " does already exist");
        }

        // update title
        folder.setTitle(newTitle);
        this.folderRepository.save(folder);

        LOG.debug("Successfully updated folder: {}", request);

    }

    @Transactional
    public void removeFolder(final RemoveFolderRequest request, final long currentUserId) {

        LOG.debug("Deleting folder: {}", request);

        // load folder
        final Folder folder = this.loadFolder(request.getFolderId(), currentUserId);
        if (folder == null) {
            throw new NotFoundException("Folder does not exist");
        }

        // delete folder
        this.folderRepository.delete(folder);

        // remove feeds without subscribers
        this.feedService.removeFeedsWithoutSubscribers();

        LOG.debug("Successfully deleted folder: {}", request);

    }

    private Folder loadFolder(final long folderId, final long currentUserId) {
        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(QFolder.folder.id.eq(folderId));
        filter.and(QFolder.folder.user.id.eq(currentUserId));
        return this.folderRepository.findOne(filter);
    }

    private boolean folderExists(final String title, final long currentUserId) {
        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(QFolder.folder.user.id.eq(currentUserId));
        filter.and(QFolder.folder.title.eq(title));
        return this.folderRepository.exists(filter);
    }

}
