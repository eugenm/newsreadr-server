package de.patrickgotthard.newsreadr.server.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.server.service.RoleService;
import de.patrickgotthard.newsreadr.shared.request.GetRolesRequest;
import de.patrickgotthard.newsreadr.shared.response.GetRolesResponse;

@RestController
@RequestMapping("/api")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(final RoleService roleService) {
        this.roleService = roleService;
    }

    @RequestMapping(params = GetRolesRequest.METHOD)
    public GetRolesResponse getRoles() {
        return roleService.getRoles();
    }

}
