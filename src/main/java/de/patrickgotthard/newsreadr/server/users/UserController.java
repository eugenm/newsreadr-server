package de.patrickgotthard.newsreadr.server.users;

import org.springframework.beans.factory.annotation.Autowired;

import de.patrickgotthard.newsreadr.server.common.web.ApiController;
import de.patrickgotthard.newsreadr.server.common.web.ApiRequestMapping;
import de.patrickgotthard.newsreadr.shared.request.AddUserRequest;
import de.patrickgotthard.newsreadr.shared.request.GetUsersRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveUserRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateUserRequest;
import de.patrickgotthard.newsreadr.shared.response.GetUsersResponse;
import de.patrickgotthard.newsreadr.shared.response.Response;

@ApiController
class UserController {

    private final UserService userService;

    @Autowired
    UserController(final UserService userService) {
        this.userService = userService;
    }

    @ApiRequestMapping(AddUserRequest.class)
    Response addUser(final AddUserRequest request) {
        return userService.addUser(request);
    }

    @ApiRequestMapping(GetUsersRequest.class)
    GetUsersResponse getUsers() {
        return userService.getUsers();
    }

    @ApiRequestMapping(UpdateUserRequest.class)
    Response updateUser(final UpdateUserRequest request) {
        return userService.updateUser(request);
    }

    @ApiRequestMapping(RemoveUserRequest.class)
    Response removeUser(final RemoveUserRequest request) {
        return userService.removeUser(request);
    }

}
