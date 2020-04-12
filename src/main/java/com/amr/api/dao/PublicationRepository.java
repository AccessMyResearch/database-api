package com.amr.api.dao;

import com.amr.api.model.Author;
import com.amr.api.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Integer> {

    List<Publication> findAllByAuthorsContains(Author author);
    List<Publication> findAllByOrderByPublicationDateDesc();
    List<Publication> findAllByPublicationDateBetween(String startDate, String endDate);

}
