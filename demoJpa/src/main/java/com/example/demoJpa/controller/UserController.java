package com.example.demoJpa.controller;


import com.example.demoJpa.dto.LoginRequest;
import com.example.demoJpa.dto.UserDto;
import com.example.demoJpa.exception.UserNotFoundException;
import com.example.demoJpa.repository.RoleRepository;
import com.example.demoJpa.service.RoleService;
import com.example.demoJpa.service.UserService;
import com.example.demoJpa.entity.User;
import com.example.demoJpa.repository.UserRepository;
import com.example.demoJpa.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

   @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDTO) {
        try {
            if (userRepository.findByUsername(userDTO.getUsername()) != null) {
                return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
            }
            User user = new User();
            user.setRole(roleRepository.findById(userDTO.getRole().getId()).orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + userDTO.getRole().getId())));
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setUsername(userDTO.getUsername());
            userRepository.save(user);
            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        } catch (HttpMessageNotReadableException ex) {
            return new ResponseEntity<>("Invalid role. Accepted roles are USER, ADMIN, MANAGER.", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            System.out.println("ok");
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(), request.getPassword()
                            )
                    );
            UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
            User user = userRepository.findByUsername(userDetails.getUsername());

            /*System.out.println(authenticate);
            User user = (User) authenticate.getPrincipal();
            System.out.println(user);
            SecurityContextHolder.getContext().setAuthentication(authenticate);*/

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtTokenUtil.generateAccessToken(user)
                    )
                    .body(user);
        } catch (BadCredentialsException | ClassCastException ex) {
            SecurityContextHolder.getContext().setAuthentication(null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

   /* @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
       System.out.println("ok");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            Utilisateur user = (Utilisateur) authenticate.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(auth);
            return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
        }
        @GetMapping("/admin/adminProfile")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public String adminProfile() {
            return "Welcome to Admin Profile";
        }*/

    @GetMapping("/user/userProfile")
   // @PreAuthorize("hasAnyAuthority('USER_READ')")
    public String userProfile() {
        return "Welcome to User Profile";
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllUsersWithTotalCount() {
     
        List<User> users = userService.findAll();
        long totalCount = users.size();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Successfully retrieved list of users");
        response.put("users", users);
        response.put("total", totalCount);

        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(totalCount)).body(response);
    }



}



