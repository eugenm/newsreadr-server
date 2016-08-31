package de.patrickgotthard.newsreadr.server.users;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.server.users.request.AddUserRequest;
import de.patrickgotthard.newsreadr.server.users.request.RemoveUserRequest;
import de.patrickgotthard.newsreadr.server.users.request.UpdateUserRequest;
import de.patrickgotthard.newsreadr.server.users.response.UserDTO;

@RestController
@RequestMapping("/api/users")
class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addUser(@Valid final AddUserRequest request, final long currentUserId) {
        this.userService.addUser(request, currentUserId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<UserDTO> getUsers(final long currentUserId) {
        return this.userService.getUsers(currentUserId);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public void updateUser(@Valid final UpdateUserRequest request, final long currentUserId) {
        this.userService.updateUser(request, currentUserId);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public void removeUser(@Valid final RemoveUserRequest request, final long currentUserId) {
        this.userService.removeUser(request, currentUserId);
    }

}
