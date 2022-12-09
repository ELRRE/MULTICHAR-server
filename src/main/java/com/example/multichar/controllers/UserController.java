package com.example.multichar.controllers;

import com.example.multichar.Entity.Tokens;
import com.example.multichar.Entity.User;
import com.example.multichar.service.JwtProvider;
import com.example.multichar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.security.auth.message.AuthException;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;


    @Autowired
    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping(value = "/info")
    public ResponseEntity<User> getUserInfo(@RequestHeader("Token") String token){
        String username = jwtProvider.getAccessSubject(token);
        User user = userService.getInfo(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @CrossOrigin
    @PostMapping(value = "/registration")
    public ResponseEntity<Tokens> registration(@RequestBody Map<String, String> body){
        User user = new User(
                body.get("name"),
                body.get("email"),
                body.get("pass")
        );
        Tokens tokens = userService.registration(user, body.get("passConfirmation"));
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }
    @CrossOrigin
    @PostMapping(value = "/login")
    public ResponseEntity<Tokens> login(@RequestBody Map<String,String> body) {
        Tokens tokens = userService.login(body.get("login"), body.get("pass"));
        return new ResponseEntity<>(tokens,HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(@RequestHeader("Token") String token){
        String username = jwtProvider.getAccessSubject(token);
        userService.logout(username);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping(value = "/tokens/refresh")
    public ResponseEntity<Tokens> refreshTokens(@RequestBody Map<String,String> body) {
        Tokens tokens = userService.refresh(body.get("refreshToken"));
        return new ResponseEntity<>(tokens,HttpStatus.OK);

    }
}
