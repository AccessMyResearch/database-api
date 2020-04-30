package com.amr.api.model;

import lombok.Data;
import lombok.Value;

import java.util.List;
import java.util.Set;

@Data
public class GetPublicationsRequest {

    Set<String> searchKeywords;
    FilterList filters;

}
