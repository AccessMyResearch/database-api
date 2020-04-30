package com.amr.api.controller;

import com.amr.api.model.*;
import com.amr.api.service.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if(isNull(openId) || openId.isEmpty())
            return ResponseEntity.badRequest().body("user open id must be specified");
        if(!openId.matches(openIdRegex))
            return ResponseEntity.badRequest().body("user open id must be valid open id format");
        UserValues response = service.getUsersByOpenId(openId);
        if(isNull(response))
            return ResponseEntity.notFound().build();
        return ResponseEntity
                .ok()
                .body(response);
    }

    @GetMapping("/getPublicationsByAuthor")
    public ResponseEntity<?> getPublicationsByAuthor(@RequestParam("author") String authorName) {
        if(isNull(authorName) || authorName.isEmpty())
            return ResponseEntity.badRequest().header("Access-Control-Allow-Origin", "*").body("author name must be specified");
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublicationsByAuthor(authorName);
        if(isNull(getPublicationsAPIResponse))
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

    @PostMapping("/addPublication")
    public ResponseEntity<String> addPublication(@RequestBody AddPublicationRequest request) {
        if(isNull(request.getTitle()) || request.getTitle().isEmpty())
            return ResponseEntity
                    .badRequest()
                    .body("Title can not be null");

        Publication publication = service.addPublication(request);
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(publication.toString());
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody AddUserRequest request) {
        User user = service.addUser(request);
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(user.toString());
    }

    @PostMapping("/addAuthor")
    public ResponseEntity<String> addAuthor(@RequestBody AddAuthorRequest request) {
        Author author = service.addAuthor(request);
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(author.toString());
    }

    /*TODO: Endpoints:
        getPublicationsTrending
        getPublicationsFilterArea
        incrementViewCount
     */
}
