package com.company.safekyc.service;

import com.company.safekyc.exception.AppException;
import com.company.safekyc.exception.ResourceNotFoundException;
import com.company.safekyc.model.Role;
import com.company.safekyc.model.RoleName;
import com.company.safekyc.model.User;
import com.company.safekyc.payload.ApiResponse;
import com.company.safekyc.payload.UserIdentityAvailability;
import com.company.safekyc.payload.UserProfile;
import com.company.safekyc.payload.UserSummary;
import com.company.safekyc.repository.RoleRepository;
import com.company.safekyc.repository.UserRepository;
import com.company.safekyc.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserSummary getCurrentUser(UserPrincipal currentUser){
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFirstName(), currentUser.getLastName());
    }

    public UserIdentityAvailability checkUsernameAvailability(String username){
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    public UserIdentityAvailability checkEmailAvailability(String email){
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    public UserProfile getUserProfile(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        return new UserProfile(user.getId(), user.getUsername(), user.getCreatedAt(), user.getEmail(), user.getPhone(), user.getNationality());
    }

    public ResponseEntity<?> addUser(User user){

        if(userRepository.existsByEmail(user.getEmail())){
            return new ResponseEntity<>(new ApiResponse(false, "Email is already taken"), HttpStatus.BAD_REQUEST);
        }

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User result =  userRepository.save(user);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateUser(User newUser, String username, UserPrincipal currentUser){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        if(user.getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))){
            user.setUsername(newUser.getUsername());
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            user.setPhone(newUser.getPhone());
            user.setNationality(newUser.getNationality());

            User updatedUser =  userRepository.save(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);

        }

        return new ResponseEntity<>(new ApiResponse(false, "You don't have permission to update profile of: " + username), HttpStatus.UNAUTHORIZED);

    }

    public ResponseEntity<?> deleteUser(String username, UserPrincipal currentUser){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "id", username));
        if(!user.getId().equals(currentUser.getId())){
            return new ResponseEntity<>(new ApiResponse(false, "You don't have permission to delete profile of: " + username), HttpStatus.UNAUTHORIZED);
        }
        userRepository.deleteById(user.getId());

        return new ResponseEntity<>(new ApiResponse(true, "You successfully deleted profile of: " + username), HttpStatus.OK);
    }

    public ResponseEntity<?> giveAdmin(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new AppException("User role not set")));
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>(new ApiResponse(true, "You gave ADMIN role to user: " + username), HttpStatus.OK);
    }

    public ResponseEntity<?> takeAdmin(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>(new ApiResponse(true, "You took ADMIN role from user: " + username), HttpStatus.OK);
    }
}
