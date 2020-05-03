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
    Publication addPublication(AddPublicationRequest addPublicationRequest);
    List<Publication> addPublications(List<AddPublicationRequest> addPublicationRequests);
    User addUser(AddUserRequest addUserRequest);
    Author addAuthor(AddAuthorRequest addAuthorRequest);
    GetPublicationsAPIResponse getPublicationsFilter(GetPublicationsRequest request);

    GetPublicationsAPIResponse getPublicationsNew(GetPublicationsRequest request);

    UserValues getUsersByOpenId(String openId);
}
