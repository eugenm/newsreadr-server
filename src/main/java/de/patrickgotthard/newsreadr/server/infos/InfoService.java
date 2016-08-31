package de.patrickgotthard.newsreadr.server.infos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserRepository;
import de.patrickgotthard.newsreadr.server.infos.response.GetInfosResponse;

@Service
class InfoService {

    private final UserRepository userRepository;

    @Autowired
    public InfoService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public GetInfosResponse getInfos(final long currentUserId) {

        final User currentUser = this.userRepository.findOne(currentUserId);

        final GetInfosResponse response = new GetInfosResponse();
        response.setUserId(currentUser.getId());
        response.setUsername(currentUser.getUsername());
        return response;

    }

}
