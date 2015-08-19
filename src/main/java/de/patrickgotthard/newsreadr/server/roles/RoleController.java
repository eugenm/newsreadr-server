package de.patrickgotthard.newsreadr.server.roles;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Role;

@RestController
@RequestMapping("/api/roles")
class RoleController {

    @RequestMapping(method = RequestMethod.GET)
    public Role[] getRoles() {
        return Role.values();
    }

}
