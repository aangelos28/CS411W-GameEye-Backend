package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import com.google.firebase.auth.FirebaseToken;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Preferences;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * REST API for interacting with user profiles.
 */
@RestController
public class UserController {

    private final UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Request body for admin endpoints.
     */
    private static class UserRequest {
        public String userId;
    }

    private static class SettingsRequest
    {
        public Preferences preferences;

        public Preferences getPreferences() {
            return preferences;
        }
    }
    /**
     * Checks if a user profile exists.
     * @return True if the user profile exists, false otherwise
     */
    @GetMapping(path = "/private/user/exists")
    public boolean checkUserExists() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();
        final String userId = fbToken.getUid();

        // Ensure that the user profile does not already exist
        return userService.checkUserExists(userId);
    }

    /**
     * Creates a user profile if it does not already exist.
     */
    @PostMapping(path = "/private/user/create")
    public ResponseEntity<String> createUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();
        final String userId = fbToken.getUid();

        // Ensure that the user profile does not already exist
        if (userService.checkUserExists(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Create the user profile
        userService.createUser(userId);
        logger.info(String.format("Created user profile with firebase id %s", userId));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping (path = "/private/settings/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> applySettings(@RequestBody SettingsRequest request)
    {


        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();

        try {
            userService.AdjustSettings(fbToken.getUid(), request.getPreferences());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Updated settings.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update settings.");
        }
    }
    /**
     * Deletes a user profile if it does not already exist.
     */
    @DeleteMapping(path = "/private/user/delete")
    public ResponseEntity<String> deleteUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();
        final String userId = fbToken.getUid();

        // Ensure that the user profile already exists
        if (!userService.checkUserExists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Delete the user profile
        userService.deleteUser(userId);
        logger.info(String.format("Deleted user profile with firebase id %s", userId));

        return ResponseEntity.ok("User profile deleted.");
    }

    /**
     * Creates a user profile if it does not already exist.
     */
    @PostMapping(path = "/private-admin/user/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUserAdmin(@RequestBody UserRequest request) {
        final String userId = request.userId;

        // Ensure that the user profile does not already exist
        if (userService.checkUserExists(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Create the user profile
        userService.createUser(userId);
        logger.info(String.format("ADMIN: Created user profile with firebase id %s", userId));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Deletes a user profile if it does not already exist.
     */
    @DeleteMapping(path = "/private-admin/user/delete")
    public ResponseEntity<String> deleteUserAdmin(@RequestBody UserRequest request) {
        final String userId = request.userId;

        // Ensure that the user profile already exists
        if (!userService.checkUserExists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Delete the user profile
        userService.deleteUser(userId);
        logger.info(String.format("ADMIN: Deleted user profile with firebase id %s", userId));

        return ResponseEntity.ok("User profile deleted.");
    }
}
