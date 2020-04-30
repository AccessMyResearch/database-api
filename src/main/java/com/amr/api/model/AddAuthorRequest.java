package com.amr.api.model;

import lombok.Value;

@Value
public class AddAuthorRequest {
    Integer userId;
    String orcidId;
    String name;
}
