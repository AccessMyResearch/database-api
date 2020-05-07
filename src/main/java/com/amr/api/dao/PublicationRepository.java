package com.amr.api.dao;

import com.amr.api.model.Author;
import com.amr.api.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Integer>, JpaSpecificationExecutor<Publication> {
    boolean existsByDoi(String doi);
    Publication findByDoi(String doi);
    List<Publication> findAllByDoiIn(Collection<String> dois);

    List<Publication> findAllByAuthorsContains(Author author);
    List<Publication> findAllByOrderByPublicationDateDesc();
    List<Publication> findAllByPublicationDateBetween(String startDate, String endDate);
}
