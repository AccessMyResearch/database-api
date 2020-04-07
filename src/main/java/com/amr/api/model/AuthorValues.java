package com.amr.api.model;

import lombok.Value;

@Value
public class AuthorValues {
    Integer userId;
    String orcidId;
    String name;
}
