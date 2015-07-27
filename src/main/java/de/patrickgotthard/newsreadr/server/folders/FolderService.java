package de.patrickgotthard.newsreadr.server.folders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.BooleanBuilder;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Folder;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QFolder;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.FolderRepository;
import de.patrickgotthard.newsreadr.server.common.rest.AlreadyExistsException;
import de.patrickgotthard.newsreadr.server.common.rest.NotFoundException;
import de.patrickgotthard.newsreadr.server.feeds.FeedService;
import de.patrickgotthard.newsreadr.server.folders.request.AddFolderRequest;
import de.patrickgotthard.newsreadr.server.folders.request.RemoveFolderRequest;
import de.patrickgotthard.newsreadr.server.folders.request.UpdateFolderRequest;

@Service
class FolderService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedService.class);

    private final FolderRepository folderRepository;
    private final FeedService feedService;

    @Autowired
    public FolderService(final FolderRepository folderRepository, final FeedService feedService) {
        this.folderRepository = folderRepository;
        this.feedService = feedService;
    }

    @Transactional
    public void addFolder(final AddFolderRequest request, final User currentUser) {

        LOG.debug("Adding folder: {}", request);
        final String title = request.getTitle();

        final boolean folderExists = this.folderExists(title, currentUser);
        if (folderExists) {
            throw new AlreadyExistsException("The folder " + title + " does already exist");
        }

        final Folder folder = new Folder.Builder().setUser(currentUser).setTitle(title).build();
        this.folderRepository.save(folder);

        LOG.debug("Successfully added folder: {}", title);

    }

    @Transactional
    public void updateFolder(final UpdateFolderRequest request, final User currentUser) {

        LOG.debug("Updating folder: {}", request);

        // load folder
        final Folder folder = this.loadFolder(request.getFolderId(), currentUser);
        if (folder == null) {
            throw new NotFoundException("Folder does not exist");
        }

        // check whether new folder name already exists
        final String newTitle = request.getTitle();

        final boolean folderAlreadyExists = this.folderExists(newTitle, currentUser);
        if (folderAlreadyExists) {
            throw new AlreadyExistsException("The folder " + newTitle + " does already exist");
        }

        // update title
        folder.setTitle(newTitle);
        this.folderRepository.save(folder);

        LOG.debug("Successfully updated folder: {}", request);

    }

    @Transactional
    public void removeFolder(final RemoveFolderRequest request, final User currentUser) {

        LOG.debug("Deleting folder: {}", request);

        // load folder
        final Folder folder = this.loadFolder(request.getFolderId(), currentUser);
        if (folder == null) {
            throw new NotFoundException("Folder does not exist");
        }

        // delete folder
        this.folderRepository.delete(folder);

        // remove feeds without subscribers
        this.feedService.removeFeedsWithoutSubscribers();

        LOG.debug("Successfully deleted folder: {}", request);

    }

    private Folder loadFolder(final long folderId, final User currentUser) {
        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(QFolder.folder.id.eq(folderId));
        filter.and(QFolder.folder.user.eq(currentUser));
        return this.folderRepository.findOne(filter);
    }

    private boolean folderExists(final String title, final User currentUser) {
        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(QFolder.folder.user.eq(currentUser));
        filter.and(QFolder.folder.title.eq(title));
        return this.folderRepository.count(filter) > 0;
    }

}
