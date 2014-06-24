package de.patrickgotthard.newsreadr.server.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.shared.request.AddUserRequest;
import de.patrickgotthard.newsreadr.shared.request.GetUsersRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveUserRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateUserRequest;
import de.patrickgotthard.newsreadr.shared.response.GetUsersResponse;
import de.patrickgotthard.newsreadr.shared.response.Response;

@RestController
@RequestMapping("/api")
class UserController {

    private final UserService userService;

    @Autowired
    UserController(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(params = AddUserRequest.METHOD)
    Response addUser(final AddUserRequest request) {
        return userService.addUser(request);
    }

    @RequestMapping(params = GetUsersRequest.METHOD)
    GetUsersResponse getUsers() {
        return userService.getUsers();
    }

    @RequestMapping(params = UpdateUserRequest.METHOD)
    Response updateUser(final UpdateUserRequest request) {
        return userService.updateUser(request);
    }

    @RequestMapping(params = RemoveUserRequest.METHOD)
    Response removeUser(final RemoveUserRequest request) {
        return userService.removeUser(request);
    }

}
