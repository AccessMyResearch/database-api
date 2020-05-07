package com.amr.api.dao;

import com.amr.api.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author findByName(String name);
    List<Author> findAllByNameIn(List<String> names);
    Author findByOrcidId(String orcidId);
    List<Author> findAllByOrcidIdIn(Collection<String> orcidIds);
    Author findByUserId(int userId);
    List<Author> findAllByUserIdIn(Collection<Integer> userIds);
}
