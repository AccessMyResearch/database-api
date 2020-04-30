package com.amr.api.model;

import lombok.Value;

@Value
public class UserValues {
    String email;
    String orcidId;
    String firstName;
    String middleName;
    String lastName;
    String openId;
}
