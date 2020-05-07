package com.amr.api.service;

import com.amr.api.model.*;

import java.util.List;

public interface Service {

    GetPublicationsAPIResponse getPublications();
    GetPublicationsAPIResponse getPublicationsByAuthor(String authorName);
    GetPublicationsAPIResponse getPublicationsRecent();
    GetPublicationsAPIResponse getPublicationsMostViewed();
    GetPublicationsAPIResponse getPublicationsByYear(Integer startYear, Integer endYear);
    GetAuthorsAPIResponse getAuthors();
    GetUsersAPIResponse getUsers();
    List<Publication> addPublications(AddPublicationRequest addPublicationRequest);
    User addUser(AddUserRequest addUserRequest);
    List<Author> addAuthors(AddAuthorRequest addAuthorRequest);
    GetPublicationsAPIResponse getPublicationsFilter(GetPublicationsRequest request);

    GetPublicationsAPIResponse getPublicationsNew(GetPublicationsRequest request);

    UserValues getUsersByOpenId(String openId);
}
