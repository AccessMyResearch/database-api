package com.amr.api.model;

import lombok.Value;

import java.util.List;

@Value
public class GetUsersAPIResponse {
    List<UserValues> users;
}
