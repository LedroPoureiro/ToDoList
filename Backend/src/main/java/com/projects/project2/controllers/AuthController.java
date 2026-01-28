package com.projects.project2.controllers;

import com.projects.project2.model.User;
import com.projects.project2.model.dto.AuthResponse;
import com.projects.project2.model.dto.ReturnTaskDto;
import com.projects.project2.model.dto.UserDto;
import com.projects.project2.repositories.UserRepository;
import com.projects.project2.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Authorization", description = "Authorization API for register and login")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    @Operation(
            summary = "Registers a new user",
            description = "Regists a new user with a username and password, if not already present in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully created a new user"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Username taken",
                    content = @Content
            ),
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        if (userRepository.findByUsername(userDto.username()).isPresent()) {
            return ResponseEntity.badRequest().body("Username taken");
        }
        User user = new User();
        user.setUsername(userDto.username());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered");
    }



    @Operation(
            summary = "Login an user",
            description = "Login an user: if already registered and password correct. Returns a JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully logged in a user, returns JWT"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Incorrect password or username not found",
                    content = @Content
            ),
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.username(), userDto.password())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}


