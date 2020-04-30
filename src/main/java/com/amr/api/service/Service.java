package com.amr.api.service;

import com.amr.api.model.*;

public interface Service {

    GetPublicationsAPIResponse getPublications();
    GetPublicationsAPIResponse getPublicationsByAuthor(String authorName);
    GetPublicationsAPIResponse getPublicationsRecent();
    GetPublicationsAPIResponse getPublicationsMostViewed();
    GetPublicationsAPIResponse getPublicationsByYear(Integer startYear, Integer endYear);
    GetAuthorsAPIResponse getAuthors();
    GetUsersAPIResponse getUsers();
    Publication addPublication(AddPublicationRequest addPublicationRequest);
    User addUser(AddUserRequest addUserRequest);
    Author addAuthor(AddAuthorRequest addAuthorRequest);
    GetPublicationsAPIResponse getPublicationsFilter(GetPublicationsRequest request);

    GetPublicationsAPIResponse getPublicationsNew(GetPublicationsRequest request);

    UserValues getUsersByOpenId(String openId);
}
