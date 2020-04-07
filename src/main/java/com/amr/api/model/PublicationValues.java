package com.amr.api.model;

import lombok.Value;

import java.util.Set;

@Value
public class PublicationValues {
    String title;
    String doi;
    String url;
    String publicationDate;
    String summary;
    Set<String> authors;
}
