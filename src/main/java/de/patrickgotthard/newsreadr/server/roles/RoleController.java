package de.patrickgotthard.newsreadr.server.roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.shared.request.GetRolesRequest;
import de.patrickgotthard.newsreadr.shared.response.GetRolesResponse;

@RestController
@RequestMapping("/api")
class RoleController {

    private final RoleService roleService;

    @Autowired
    RoleController(final RoleService roleService) {
        this.roleService = roleService;
    }

    @RequestMapping(params = GetRolesRequest.METHOD)
    GetRolesResponse getRoles() {
        return roleService.getRoles();
    }

}
