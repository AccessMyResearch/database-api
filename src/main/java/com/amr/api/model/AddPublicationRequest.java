package com.amr.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.beans.ConstructorProperties;
import java.util.List;

@Data
@AllArgsConstructor
public class AddPublicationRequest {
    private List<AddPublicationRequestItem> items;
    private AddRequestBehavior behavior;
}
