package com.powerledger.energyprovider.security.login.controllers;

/**
 * @author Meena Shah
 */


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.powerledger.energyprovider.security.login.models.ERole;
import com.powerledger.energyprovider.security.login.models.Role;
import com.powerledger.energyprovider.security.login.models.User;
import com.powerledger.energyprovider.security.login.payload.request.LoginRequest;
import com.powerledger.energyprovider.security.login.payload.request.SignupRequest;
import com.powerledger.energyprovider.security.login.payload.response.MessageResponse;
import com.powerledger.energyprovider.security.login.payload.response.UserInfoResponse;
import com.powerledger.energyprovider.security.login.repository.RoleRepository;
import com.powerledger.energyprovider.security.login.repository.UserRepository;
import com.powerledger.energyprovider.security.login.security.jwt.JwtUtils;
import com.powerledger.energyprovider.security.login.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        UserInfoResponse UserInfoResponse = new UserInfoResponse();
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateToken(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        UserInfoResponse.setId(userDetails.getId());
        UserInfoResponse.setUsername(userDetails.getUsername());

        UserInfoResponse.setEmail(userDetails.getEmail());

        UserInfoResponse.setRoles(roles);

        UserInfoResponse.setJwtToken(jwtToken);

        return ResponseEntity.ok()
                .body(UserInfoResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        MessageResponse messageResponse = new MessageResponse();
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            messageResponse.setMessage("Already Created");
            return ResponseEntity.badRequest().body(messageResponse);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            messageResponse.setMessage("Already Created");
            return ResponseEntity.badRequest().body(messageResponse);
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();


        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {

                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        System.out.println("role "+userRepository.existsByUsername("admin"));

                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        messageResponse.setMessage("Created");
        return ResponseEntity.ok(messageResponse);
    }


}
