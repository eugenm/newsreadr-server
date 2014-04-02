package de.patrickgotthard.newsreadr.server.service;

import org.springframework.stereotype.Service;

import de.patrickgotthard.newsreadr.shared.response.GetRolesResponse;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

@Service
public class RoleService {

    public GetRolesResponse getRoles() {
        final Role[] roles = Role.values();
        return new GetRolesResponse(roles);
    }

}
