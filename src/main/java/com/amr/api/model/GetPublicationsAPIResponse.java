package com.amr.api.model;

import lombok.Value;

import java.util.List;

@Value
public class GetPublicationsAPIResponse {
    List<PublicationValues> publications;
}
