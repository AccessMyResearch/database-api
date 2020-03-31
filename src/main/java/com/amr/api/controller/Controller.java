package com.amr.api.controller;

import com.amr.api.model.GetAuthorsAPIResponse;
import com.amr.api.model.GetPublicationsAPIResponse;
import com.amr.api.model.GetUsersAPIResponse;
import com.amr.api.service.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {
    private final Service service;

    @GetMapping("/test")
    public ResponseEntity<GetPublicationsAPIResponse> test(){
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublications();
        return new ResponseEntity<>(getPublicationsAPIResponse, HttpStatus.OK);
    }

    @GetMapping("/getPublications")
    public ResponseEntity<GetPublicationsAPIResponse> getPublications(){
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublications();
        return new ResponseEntity<>(getPublicationsAPIResponse, HttpStatus.OK);
    }

    @GetMapping("/getAuthors")
    public ResponseEntity<GetAuthorsAPIResponse> getAuthors(){
        GetAuthorsAPIResponse getAuthorsAPIResponse = service.getAuthors();
        return new ResponseEntity<>(getAuthorsAPIResponse, HttpStatus.OK);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<GetUsersAPIResponse> getUsers(){
        GetUsersAPIResponse getUsersAPIResponse = service.getUsers();
        return new ResponseEntity<>(getUsersAPIResponse, HttpStatus.OK);
    }
}
