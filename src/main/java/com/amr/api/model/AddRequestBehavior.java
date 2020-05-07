package com.amr.api.model;

import lombok.Data;

@Data
public class AddRequestBehavior {
    boolean autofill; // if true, attempts to add missing fields by querying external APIs (e.g. Crossref, ORCID)
    HandleDuplicateBehavior duplicateBehavior; // determines what action to take if attempting to add a duplicate item
}
