package com.amr.api.service;

import com.amr.api.model.GetAuthorsAPIResponse;
import com.amr.api.model.GetPublicationsAPIResponse;
import com.amr.api.model.GetUsersAPIResponse;

public interface Service {
    GetPublicationsAPIResponse getPublications();
    GetAuthorsAPIResponse getAuthors();
    GetUsersAPIResponse getUsers();
}
