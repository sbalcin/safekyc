package com.company.safekyc.controller;

import com.company.safekyc.exception.AppException;
import com.company.safekyc.model.QRCode;
import com.company.safekyc.model.Role;
import com.company.safekyc.model.RoleName;
import com.company.safekyc.model.User;
import com.company.safekyc.payload.ApiResponse;
import com.company.safekyc.payload.JwtAuthenticationResponse;
import com.company.safekyc.payload.LoginRequest;
import com.company.safekyc.payload.SignUpRequest;
import com.company.safekyc.repository.QRCodeRepository;
import com.company.safekyc.repository.RoleRepository;
import com.company.safekyc.repository.UserRepository;
import com.company.safekyc.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final QRCodeRepository qrCodeRepository;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){

        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            return new ResponseEntity<>(new ApiResponse(false, "Email is already taken"), HttpStatus.BAD_REQUEST);
        }

        String username = signUpRequest.getUsername();

        String email = signUpRequest.getEmail().toLowerCase();

        User user = new User(username, email, signUpRequest.getPassword(), signUpRequest.getNationality(), signUpRequest.getPhoneNumber(), signUpRequest.getBirthDate(), signUpRequest.getClientIp());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<Role> byName = roleRepository.findByName(RoleName.ROLE_USER);
        if(!byName.isPresent())
            initRoles();

        List<Role> roles = new ArrayList<>();
        if(userRepository.count() == 0){
            roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new AppException("User role not set")));
        } else{
            roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
        }

        user.setRoles(roles);

        User result = userRepository.save(user);

        QRCode qrCode = new QRCode();
        qrCode.setUser(result);
        qrCode.setUuid(UUID.randomUUID().toString());
        qrCode.setCreationDate(new Date());
        qrCode.setStatus("active");
        qrCodeRepository.save(qrCode);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{userId}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    private void initRoles() {
        Role s = new Role();
        s.setName(RoleName.ROLE_ADMIN);
        roleRepository.save(s);

        s.setName(RoleName.ROLE_USER);
        roleRepository.save(s);
    }
}
