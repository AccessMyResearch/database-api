package com.amr.api.dao;

import com.amr.api.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Objects.isNull;
import static org.springframework.data.jpa.domain.Specification.where;

@Component
@RequiredArgsConstructor
public class DAOImpl implements DAO {
    private final PublicationRepository publicationRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;

    private static Specification<Publication> publicationMatchesKeyword(String keyword) {
        return (publication, cq, cb) -> cb.or(cb.like(cb.lower(publication.get("title")), "%".concat(keyword).concat("%")), cb.like(cb.lower(publication.get("summary")), "%".concat(keyword).concat("%")));
    }

    private static Specification<Publication> publicationHasAuthor(Author author) {
        return (publication, cq, cb) -> cb.isMember(author, publication.get("authors"));
    }

    private static Specification<Publication> publicationDateFrom(Integer startYear) {
        return (publication, cq, cb) -> cb.greaterThanOrEqualTo(publication.get("publicationDate"), startYear.toString().concat("-01-01"));
    }

    private static Specification<Publication> publicationDateTo(Integer endYear) {
        return (publication, cq, cb) -> cb.lessThanOrEqualTo(publication.get("publicationDate"), endYear.toString().concat("-12-31"));
    }

    @Override
    public List<Publication> getPublications() {
        return publicationRepository.findAll();
    }

    @Override
    public List<Publication> getPublicationsByAuthor(String authorName) {
        Author author = authorRepository.findByName(authorName);
        return publicationRepository.findAllByAuthorsContains(author);
    }

    @Override
    public List<Publication> getPublicationsRecent() {
        return publicationRepository.findAllByOrderByPublicationDateDesc();
    }

    @Override
    public List<Publication> getPublicationsByYear(Integer startYear, Integer endYear) { //TODO Check clusivity by testing a publication on Dec. 31
        return publicationRepository.findAll(publicationDateFrom(startYear).and(publicationDateTo(endYear)));
    }

    @Override
    public List<Publication> getPublicationsFilter(FilterList filters) {
        Specification<Publication> specification = where(null);

        if(!isNull(filters.getAuthor())){
            Author author = authorRepository.findByName(filters.getAuthor());
            specification = publicationHasAuthor(author).and(specification);
        }

        if(!isNull(filters.getStartYear()))
            specification = publicationDateFrom(filters.getStartYear()).and(specification);

        if(!isNull(filters.getEndYear()))
            specification = publicationDateTo(filters.getEndYear()).and(specification);

        return publicationRepository.findAll(specification);
    }

    @Override
    public User getUserByOpenId(String openId) {
        return userRepository.getUserByOpenId(openId);
    }

    @Override
    public List<Publication> getPublicationsNew(GetPublicationsRequest getPublicationsRequest) {
        Specification<Publication> specification = where(null);

        if(!isNull(getPublicationsRequest.getFilters())) {
            FilterList filters = getPublicationsRequest.getFilters();

            if (!isNull(filters.getAuthor())) {
                Author author = authorRepository.findByName(filters.getAuthor());
                specification = publicationHasAuthor(author).and(specification);
            }

            if (!isNull(filters.getStartYear()))
                specification = publicationDateFrom(filters.getStartYear()).and(specification);

            if (!isNull(filters.getEndYear()))
                specification = publicationDateTo(filters.getEndYear()).and(specification);
        }

        if(!isNull(getPublicationsRequest.getSearchKeywords())) {
            Specification<Publication> combinedSearchSpecification = getPublicationsRequest.getSearchKeywords().stream().map(DAOImpl::publicationMatchesKeyword).reduce(where(null), (searchSpecification, keywordSpecification) -> searchSpecification = keywordSpecification.or(searchSpecification));
            specification = combinedSearchSpecification.and(specification);
        }

        return publicationRepository.findAll(specification);
    }

    @Override
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Publication addPublication(Publication publication) {
        return publicationRepository.saveAndFlush(publication);
    }

    @Override
    public List<Publication> addPublications(Iterable<Publication> publications) {
        List<Publication> ret = publicationRepository.saveAll(publications);
        publicationRepository.flush();
        return ret;
    }

    @Override
    public User addUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public Author addAuthor(Author author) {
        return authorRepository.saveAndFlush(author);
    }
}
