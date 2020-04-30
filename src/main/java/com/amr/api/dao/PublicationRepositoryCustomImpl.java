package com.amr.api.dao;

import com.amr.api.model.Author;
import com.amr.api.model.FilterList;
import com.amr.api.model.Publication;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Repository
public class PublicationRepositoryCustomImpl implements PublicationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Publication> findAllWithFilters(FilterList filters) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Publication> query = criteriaBuilder.createQuery(Publication.class);
        Root<Publication> publication = query.from(Publication.class);
        List<Predicate> predicates = new ArrayList<>();

        if(!isNull(filters.getAuthor())){
            CriteriaQuery<Author> authorQuery = criteriaBuilder.createQuery(Author.class);
            Root<Author> authorRoot = authorQuery.from(Author.class);
            authorQuery.where(criteriaBuilder.equal(authorRoot.get("name"), filters.getAuthor()));
            Author author = entityManager.createQuery(authorQuery).getSingleResult();
            predicates.add(criteriaBuilder.isMember(author, publication.get("authors")));
        }

        if(!isNull(filters.getStartYear()))
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(publication.get("publicationDate"), filters.getStartYear().toString().concat("-01-01")));

        if(!isNull(filters.getEndYear()))
            predicates.add(criteriaBuilder.lessThanOrEqualTo(publication.get("publicationDate"), filters.getEndYear().toString().concat("-12-31")));

        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();

    }
}
