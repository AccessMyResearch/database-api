package com.amr.api.model;

import lombok.Value;

@Value
public class AddPublicationRequest {
    String title;
    String doi;
    String url;
    String publicationDate;
    String summary;

    boolean autofill; // if true, missing fields are retrieved from Crossref API using DOI
}
