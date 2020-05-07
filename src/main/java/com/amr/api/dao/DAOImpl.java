package com.amr.api.dao;

import com.amr.api.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.amr.Utils.isNullOrEmpty;
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

        if (!isNull(filters.getAuthor())) {
            Author author = authorRepository.findByName(filters.getAuthor());
            specification = publicationHasAuthor(author).and(specification);
        }

        if (!isNull(filters.getStartYear()))
            specification = publicationDateFrom(filters.getStartYear()).and(specification);

        if (!isNull(filters.getEndYear()))
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

        if (!isNull(getPublicationsRequest.getFilters())) {
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

        if (!isNull(getPublicationsRequest.getSearchKeywords())) {
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
    public List<Publication> addPublications(Collection<Publication> publications, HandleDuplicateBehavior duplicateBehavior) {
        if (HandleDuplicateBehavior.ERROR.equals(duplicateBehavior)) {
            List<Publication> ret = publicationRepository.saveAll(publications);
            publicationRepository.flush();
            return ret;
        }
        // merge publications with identical DOIs
        BinaryOperator<Publication> merger; // 1st param is old, 2nd param is new -- never modify 2nd param
        switch (duplicateBehavior) {
            case IGNORE:
                merger = (oldPub, newPub) -> oldPub;
                break;
            case MERGE:
                merger = (oldPub, newPub) -> {
                    if (isNullOrEmpty(oldPub.getTitle()))
                        oldPub.setTitle(newPub.getTitle());
                    if (isNullOrEmpty(oldPub.getUrl()))
                        oldPub.setUrl(newPub.getUrl());
                    if (isNullOrEmpty(oldPub.getPublicationDate()))
                        oldPub.setPublicationDate(newPub.getPublicationDate());
                    if (isNullOrEmpty(oldPub.getSummary()))
                        oldPub.setSummary(newPub.getSummary());
                    if (isNullOrEmpty(oldPub.getAuthors()))
                        oldPub.setAuthors(newPub.getAuthors());
                    return oldPub;
                };
                break;
            case UPDATE:
                merger = (oldPub, newPub) -> {
                    if (!isNullOrEmpty(newPub.getTitle()))
                        oldPub.setTitle(newPub.getTitle());
                    if (!isNullOrEmpty(newPub.getUrl()))
                        oldPub.setUrl(newPub.getUrl());
                    if (!isNullOrEmpty(newPub.getPublicationDate()))
                        oldPub.setPublicationDate(newPub.getPublicationDate());
                    if (!isNullOrEmpty(newPub.getSummary()))
                        oldPub.setSummary(newPub.getSummary());
                    if (!isNullOrEmpty(newPub.getAuthors()))
                        oldPub.setAuthors(newPub.getAuthors());
                    return oldPub;
                };
                break;
            case OVERWRITE:
                merger = (oldPub, newPub) -> {
                    oldPub.setTitle(newPub.getTitle());
                    oldPub.setUrl(newPub.getUrl());
                    oldPub.setPublicationDate(newPub.getPublicationDate());
                    oldPub.setSummary(newPub.getSummary());
                    oldPub.setAuthors(newPub.getAuthors());
                    return oldPub;
                };
                break;
            default:
                throw new IllegalStateException(); // this should never occur
        }
        List<Publication> oldPublicationsList = publicationRepository.findAllByDoiIn(
                publications.stream()
                        .map(Publication::getDoi)
                        .filter((doi) -> !isNullOrEmpty(doi))
                        .collect(Collectors.toSet()));
        Map<String, Publication> oldPublications = oldPublicationsList.stream()
                .collect(Collectors.toMap(Publication::getDoi, Function.identity()));
        List<Publication> mergedPublications = publications.stream()
                .map((newPub) -> {
                    Publication oldPub = oldPublications.get(newPub.getDoi());
                    if (isNull(oldPub))
                        return newPub;
                    return merger.apply(oldPub, newPub);
                }).collect(Collectors.toList());
        mergedPublications.forEach((pub) -> {
            if (!isNullOrEmpty(pub.getAuthors()))
                pub.setAuthors(new HashSet<>(addAuthors(pub.getAuthors(), HandleDuplicateBehavior.IGNORE)));
        });
        List<Publication> ret = publicationRepository.saveAll(mergedPublications);
        publicationRepository.flush();
        return ret;
    }

    @Override
    public User addUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public List<Author> addAuthors(Collection<Author> authors, HandleDuplicateBehavior duplicateBehavior) {
        if (Arrays.asList(
                HandleDuplicateBehavior.MERGE,
                HandleDuplicateBehavior.UPDATE,
                HandleDuplicateBehavior.OVERWRITE
        ).contains(duplicateBehavior))
            throw new NotImplementedException(); // TODO implement missing HandleDuplicateBehavior values
        if (HandleDuplicateBehavior.ERROR.equals(duplicateBehavior)) {
            List<Author> ret = authorRepository.saveAll(authors);
            publicationRepository.flush();
            return ret;
        }
        List<Author> mergedAuthors = authors.stream()
                .map((newAuthor) -> {
                    Author ret = null;
                    if (isNull(ret) && !isNull(newAuthor.getUserId()))
                        authorRepository.findByUserId(newAuthor.getUserId());
                    if (isNull(ret) && !isNullOrEmpty(newAuthor.getOrcidId()))
                        ret = authorRepository.findByOrcidId(newAuthor.getOrcidId());
                    if (isNull(ret) && !isNullOrEmpty(newAuthor.getName()))
                        ret = authorRepository.findByName(newAuthor.getName());
                    if (isNull(ret))
                        ret = newAuthor;
                    return ret;
                }).collect(Collectors.toList());
        List<Author> ret = authorRepository.saveAll(mergedAuthors);
        authorRepository.flush();
        return ret;
    }
}
