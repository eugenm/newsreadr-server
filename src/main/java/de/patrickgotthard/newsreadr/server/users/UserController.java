package de.patrickgotthard.newsreadr.server.users;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
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
    public void addUser(@Valid final AddUserRequest request, final User currentUser) {
        this.userService.addUser(request, currentUser);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<UserDTO> getUsers(final User user) {
        return this.userService.getUsers(user);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public void updateUser(@Valid final UpdateUserRequest request, final User currentUser) {
        this.userService.updateUser(request, currentUser);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public void removeUser(@Valid final RemoveUserRequest request, final User currentUser) {
        this.userService.removeUser(request, currentUser);
    }

}
