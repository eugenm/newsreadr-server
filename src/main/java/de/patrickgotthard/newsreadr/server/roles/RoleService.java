package de.patrickgotthard.newsreadr.server.roles;

import org.springframework.stereotype.Service;

import de.patrickgotthard.newsreadr.shared.response.GetRolesResponse;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

@Service
class RoleService {

    GetRolesResponse getRoles() {
        final Role[] roles = Role.values();
        return new GetRolesResponse(roles);
    }

}
