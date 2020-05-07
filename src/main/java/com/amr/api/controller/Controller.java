package com.amr.api.controller;

import com.amr.api.model.*;
import com.amr.api.service.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.amr.Utils.isNullOrEmpty;
import static java.util.Objects.isNull;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {
    private final Service service;

    private static final String openIdRegex = "[\\w-]{1,36}";

    @PostMapping("/getPublicationsFilter")
    public ResponseEntity<?> getPublicationsFilter(@RequestBody GetPublicationsRequest request) {
        GetPublicationsAPIResponse response = service.getPublicationsFilter(request);
        return ResponseEntity
                .ok()
                .body(response);
    }

    @PostMapping("/getPublications")
    public ResponseEntity<?> getPublications(@RequestBody GetPublicationsRequest request) {
        GetPublicationsAPIResponse response = service.getPublicationsNew(request);
        return ResponseEntity
                .ok()
                .body(response);
    }

    @GetMapping("/getUser/{openId}")
    public ResponseEntity<?> getUserByOpenID(@PathVariable String openId) {
        if (isNullOrEmpty(openId))
            return ResponseEntity.badRequest().body("user open id must be specified");
        if (!openId.matches(openIdRegex))
            return ResponseEntity.badRequest().body("user open id must be valid open id format");
        UserValues response = service.getUsersByOpenId(openId);
        if (isNull(response))
            return ResponseEntity.notFound().build();
        return ResponseEntity
                .ok()
                .body(response);
    }

    @GetMapping("/getPublicationsByAuthor")
    public ResponseEntity<?> getPublicationsByAuthor(@RequestParam("author") String authorName) {
        if (isNullOrEmpty(authorName))
            return ResponseEntity.badRequest().header("Access-Control-Allow-Origin", "*").body("author name must be specified");
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublicationsByAuthor(authorName);
        if (isNull(getPublicationsAPIResponse))
            return ResponseEntity.notFound().header("Access-Control-Allow-Origin", "*").build(); //TODO Consider returning empty set, rather than not found
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(getPublicationsAPIResponse);
    }

    @GetMapping("/getPublicationsRecent")
    public ResponseEntity<GetPublicationsAPIResponse> getPublicationsRecent() {
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublicationsRecent();
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(getPublicationsAPIResponse);
    }

    @GetMapping("/getPublicationsMostViewed")
    public ResponseEntity<GetPublicationsAPIResponse> getPublicationsMostViewed() {
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublicationsMostViewed();
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(getPublicationsAPIResponse);
    }

    @GetMapping("/getAuthors")
    public ResponseEntity<GetAuthorsAPIResponse> getAuthors() {
        GetAuthorsAPIResponse getAuthorsAPIResponse = service.getAuthors();
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(getAuthorsAPIResponse);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<GetUsersAPIResponse> getUsers() {
        GetUsersAPIResponse getUsersAPIResponse = service.getUsers();
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(getUsersAPIResponse);
    }

    @PostMapping(value = "/addPublications", produces = "application/json")
    public ResponseEntity<List<Publication>> addPublications(@RequestBody AddPublicationRequest request) {
        List<Publication> publication = service.addPublications(request);
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(publication);
    }

    @PostMapping(value = "/addUser", produces = "application/json")
    public ResponseEntity<User> addUser(@RequestBody AddUserRequest request) {
        User user = service.addUser(request);
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(user);
    }

    @PostMapping(value = "/addAuthors", produces = "application/json")
    public ResponseEntity<List<Author>> addAuthors(@RequestBody AddAuthorRequest request) {
        List<Author> authors = service.addAuthors(request);
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(authors);
    }

    /*TODO: Endpoints:
        getPublicationsTrending
        getPublicationsFilterArea
        incrementViewCount
     */
}
