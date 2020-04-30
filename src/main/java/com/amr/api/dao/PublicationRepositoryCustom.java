package com.amr.api.dao;

import com.amr.api.model.FilterList;
import com.amr.api.model.Publication;

import java.util.List;

public interface PublicationRepositoryCustom {
    List<Publication> findAllWithFilters(FilterList filters);
}
