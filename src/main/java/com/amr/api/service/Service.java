package com.amr.api.service;

import com.amr.api.model.*;

public interface Service {
    GetPublicationsAPIResponse getPublications();
    GetAuthorsAPIResponse getAuthors();
    GetUsersAPIResponse getUsers();
    boolean addPublication(AddPublicationRequest addPublicationRequest);
    boolean addUser(AddUserRequest addUserRequest);

    GetPublicationsAPIResponse getPublicationsByAuthor(String authorName);

    GetPublicationsAPIResponse getPublicationsRecent();

    GetPublicationsAPIResponse getPublicationsMostViewed();

    GetPublicationsAPIResponse getPublicationsByYear(Integer startYear, Integer endYear);
}
