package com.company.safekyc.controller;

import com.company.safekyc.model.User;
import com.company.safekyc.payload.UserIdentityAvailability;
import com.company.safekyc.payload.UserProfile;
import com.company.safekyc.payload.UserSummary;
import com.company.safekyc.security.CurrentUser;
import com.company.safekyc.security.UserPrincipal;
import com.company.safekyc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser){
        return userService.getCurrentUser(currentUser);
    }

    @GetMapping("/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username){
        return userService.checkUsernameAvailability(username);
    }

    @GetMapping("/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email){
        return userService.checkEmailAvailability(email);
    }

    @GetMapping("/{username}/profile")
    public UserProfile getUSerProfile(@PathVariable(value = "username") String username){
        return userService.getUserProfile(username);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user){
        return userService.addUser(user);
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User newUser, @PathVariable(value = "username") String username, @CurrentUser UserPrincipal currentUser){
        return userService.updateUser(newUser, username, currentUser);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "username") String username, @CurrentUser UserPrincipal currentUser){
        return userService.deleteUser(username, currentUser);
    }

    @PutMapping("/{username}/giveAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> giveAdmin(@PathVariable(name = "username") String username){
        return userService.giveAdmin(username);
    }

    @PutMapping("/{username}/takeAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> takeAdmin(@PathVariable(name = "username") String username){
        return userService.takeAdmin(username);
    }

}
