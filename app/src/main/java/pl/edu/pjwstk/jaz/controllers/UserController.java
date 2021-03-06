package pl.edu.pjwstk.jaz.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.exceptions.UnauthorizedException;
import pl.edu.pjwstk.jaz.database.services.UserService;
import pl.edu.pjwstk.jaz.database.entities.User;
import pl.edu.pjwstk.jaz.exceptions.UserDoesNotExistException;

@RestController
public class UserController {
    private final UserService userService;


    public UserController(UserService userRepository) {
        this.userService = userRepository;
    }

    @GetMapping("/{username}")
    public User getUserEntity(@PathVariable String username) throws UnauthorizedException, UserDoesNotExistException {
        User currentUser =
        (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if(currentUser.getUsername().equals(username) || currentUser.getAuthority().contains("admin"))
            return userService.findUserByUsername(username);
        throw new UnauthorizedException();
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/deleteUser/{username}")
    public void deleteUser(@PathVariable String username) throws UserDoesNotExistException {
       userService.deleteUser(username);
    }

}

