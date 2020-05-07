package com.amr.api.model;

import lombok.Value;

@Value
public class AddPublicationRequestItem {
    String title;
    String doi;
    String url;
    String publicationDate;
    String summary;
}
