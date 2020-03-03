package com.amr.api.controller;

import com.amr.api.model.PublicationAPIResponse;
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
    public ResponseEntity<PublicationAPIResponse> test(){
        PublicationAPIResponse publicationAPIResponse = service.getPublications();
        return new ResponseEntity<>(publicationAPIResponse, HttpStatus.OK);
    }
}
