package com.amr.api.controller;

import com.amr.api.model.*;
import com.amr.api.service.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.isNull;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {
    private final Service service;

    @GetMapping("/getPublications")
    public ResponseEntity<GetPublicationsAPIResponse> getPublications() {
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublications();
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(getPublicationsAPIResponse);
    }

    @GetMapping("/getPublicationsByAuthor")
    public ResponseEntity<GetPublicationsAPIResponse> getPublicationsByAuthor(@RequestParam("author") String authorName) {
        if(isNull(authorName))
            return ResponseEntity.badRequest().header("Access-Control-Allow-Origin", "*").build();
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

    @GetMapping("/getPublicationsByYear")
    public ResponseEntity<GetPublicationsAPIResponse> getPublicationsByYear(@RequestParam("start_year") Integer startYear, @RequestParam("end_year") Integer endYear) {
        if(isNull(startYear) || isNull(endYear) || endYear < startYear)
            return ResponseEntity.badRequest().header("Access-Control-Allow-Origin", "*").build();
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublicationsByYear(startYear, endYear);
//        if(isNull(getPublicationsAPIResponse))
//            return ResponseEntity.notFound().header("Access-Control-Allow-Origin", "*").build();
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
    public ResponseEntity<Boolean> addPublication(@RequestBody AddPublicationRequest request) {
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(true);
    }

    @PostMapping("/addUser")
    public ResponseEntity<Boolean> addUser(@RequestBody AddUserRequest request) {
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(true);
    }

    /*TODO: Endpoints:
        getPublicationsTrending
        getPublicationsFilterArea
        incrementViewCount
     */
}
