package de.patrickgotthard.newsreadr.server.roles;

import org.springframework.beans.factory.annotation.Autowired;

import de.patrickgotthard.newsreadr.server.common.web.ApiController;
import de.patrickgotthard.newsreadr.server.common.web.ApiRequestMapping;
import de.patrickgotthard.newsreadr.shared.request.GetRolesRequest;
import de.patrickgotthard.newsreadr.shared.response.GetRolesResponse;

@ApiController
class RoleController {

    private final RoleService roleService;

    @Autowired
    RoleController(final RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiRequestMapping(GetRolesRequest.class)
    GetRolesResponse getRoles() {
        return roleService.getRoles();
    }

}
