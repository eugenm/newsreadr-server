package de.patrickgotthard.newsreadr.server.roles;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import de.patrickgotthard.newsreadr.shared.response.GetRolesResponse;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

@Service
class RoleService {

    GetRolesResponse getRoles() {
        final Role[] roles = Role.values();
        final List<Role> list = Arrays.asList(roles);
        return new GetRolesResponse(list);
    }

}
