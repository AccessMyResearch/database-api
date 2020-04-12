package com.amr.api.model;

import lombok.Value;

import javax.persistence.Column;

@Value
public class AddUserRequest {
    String email;

    String orcidId;
    String firstName;
    String middleName;
    String lastName;
}
