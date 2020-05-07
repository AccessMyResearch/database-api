package com.amr.api.model;

import lombok.Value;

@Value
public class AddAuthorRequestItem {
    Integer userId;
    String orcidId;
    String name;
}
