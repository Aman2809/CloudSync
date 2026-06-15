package com.project.cloudsync.service;

import com.project.cloudsync.dtos.ProviderResponse;
import com.project.cloudsync.dtos.UserDto;
import com.project.cloudsync.entities.User;
import com.project.cloudsync.repositories.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Find user by Google Drive access token
     */
    public Optional<User> findByGoogleAccessToken(String googleAccessToken) {
        return userRepository.findByGoogleAccessToken(googleAccessToken);
    }

    /**
     * Find user by Dropbox access token
     */
    public Optional<User> findByDropboxAccessToken(String dropboxAccessToken) {
        return userRepository.findByDropboxAccessToken(dropboxAccessToken);
    }

    /**
     * Find user by both tokens - useful for bidirectional sync
     */
    public Optional<User> findByTokens(String gdriveToken, String dropboxToken) {
        // First try to find by Google Drive token
        Optional<User> userByGDrive = findByGoogleAccessToken(gdriveToken);
        if (userByGDrive.isPresent()) {
            User user = userByGDrive.get();
            // Verify this user also has the dropbox token
            if (dropboxToken.equals(user.getDropboxAccessToken())) {
                return userByGDrive;
            }
        }

        // Try to find by Dropbox token
        Optional<User> userByDropbox = findByDropboxAccessToken(dropboxToken);
        if (userByDropbox.isPresent()) {
            User user = userByDropbox.get();
            // Verify this user also has the gdrive token
            if (gdriveToken.equals(user.getGoogleAccessToken())) {
                return userByDropbox;
            }
        }

        return Optional.empty();
    }

    /**
     * Save user
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Update last synced time
     */
    public void updateLastSyncedTime(User user) {
        user.setLastSyncedAt(java.time.LocalDateTime.now());
        userRepository.save(user);
    }



    // For /user endpoint
    public UserDto getCurrentUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }


    public ProviderResponse getConnectedProviders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> providers = new ArrayList<>();

        if (user.getGoogleAccessToken() != null) {
            providers.add("GOOGLE");
        }
        if (user.getDropboxAccessToken() != null) {
            providers.add("DROPBOX");
        }

        return new ProviderResponse(providers);
    }


}