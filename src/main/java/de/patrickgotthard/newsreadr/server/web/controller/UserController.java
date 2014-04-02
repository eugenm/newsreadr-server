package de.patrickgotthard.newsreadr.server.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.server.service.UserService;
import de.patrickgotthard.newsreadr.shared.request.AddUserRequest;
import de.patrickgotthard.newsreadr.shared.request.GetUsersRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveUserRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateUserRequest;
import de.patrickgotthard.newsreadr.shared.response.GetUsersResponse;
import de.patrickgotthard.newsreadr.shared.response.Response;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(params = AddUserRequest.METHOD)
    public Response addUser(final AddUserRequest request) {
        return userService.addUser(request);
    }

    @RequestMapping(params = GetUsersRequest.METHOD)
    public GetUsersResponse getUsers() {
        return userService.getUsers();
    }

    @RequestMapping(params = UpdateUserRequest.METHOD)
    public Response updateUser(final UpdateUserRequest request) {
        return userService.updateUser(request);
    }

    @RequestMapping(params = RemoveUserRequest.METHOD)
    public Response removeUser(final RemoveUserRequest request) {
        return userService.removeUser(request);
    }

}
