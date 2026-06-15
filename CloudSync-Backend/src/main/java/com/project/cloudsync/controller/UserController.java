package com.project.cloudsync.controller;


import com.project.cloudsync.dtos.ProviderResponse;
import com.project.cloudsync.dtos.UserDto;
import com.project.cloudsync.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserService userService;

    public UserController(OAuth2AuthorizedClientService authorizedClientService, UserService userService) {
        this.authorizedClientService = authorizedClientService;
        this.userService=userService;
    }

    // ✅ Return basic user info
    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserInfo(@AuthenticationPrincipal OAuth2User user) {
        return ResponseEntity.ok(userService.getCurrentUser(user));
    }


    @GetMapping("/users/{id}/providers")
    public ResponseEntity<ProviderResponse> getUserProviders(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getConnectedProviders(id));
    }

}
