package com.amr.api.model;

import lombok.Value;

@Value
public class Publication {
    String publicationID;
    String primaryAuthor;
    String title;
    String doi;
    String link;
}
