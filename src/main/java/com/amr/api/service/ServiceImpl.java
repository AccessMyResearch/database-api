package com.amr.api.service;

import com.amr.api.dao.DAO;
import com.amr.api.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceImpl implements com.amr.api.service.Service {
    private final DAO dao;

    @Override
    public GetPublicationsAPIResponse getPublications() {
        List<Publication> publications = dao.getPublications();
        return publicationToPublicationValue(publications);
    }

    @Override
    public GetPublicationsAPIResponse getPublicationsByAuthor(String authorName) {
        List<Publication> publications = dao.getPublicationsByAuthor(authorName);
        return publicationToPublicationValue(publications);
    }

    @Override
    public GetPublicationsAPIResponse getPublicationsRecent() {
        List<Publication> publications = dao.getPublicationsRecent();
        return publicationToPublicationValue(publications);
    }

    @Override
    public GetPublicationsAPIResponse getPublicationsMostViewed() {
        return null;
    }

    @Override
    public GetPublicationsAPIResponse getPublicationsByYear(Integer startYear, Integer endYear) {
        List<Publication> publications = dao.getPublicationsByYear(startYear, endYear);
        return publicationToPublicationValue(publications);
    }

    @Override
    public GetAuthorsAPIResponse getAuthors() {
        List<Author> authors = dao.getAuthors();
        List<AuthorValues> authorValues = authors.stream().map(author -> new AuthorValues(author.getUserId(), author.getOrcidId(), author.getName())).collect(Collectors.toList());
        return new GetAuthorsAPIResponse(authorValues);
    }

    @Override
    public GetUsersAPIResponse getUsers() {
        List<User> users = dao.getUsers();
        List<UserValues> userValues = users.stream().map(user -> new UserValues(user.getEmail(), user.getOrcidId(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getOpenId())).collect(Collectors.toList());
        return new GetUsersAPIResponse(userValues);
    }

    @Override
    public Publication addPublication(AddPublicationRequest addPublicationRequest) {
        Publication publication = new Publication(null, addPublicationRequest.getTitle(), addPublicationRequest.getDoi(), addPublicationRequest.getUrl(), addPublicationRequest.getPublicationDate(), addPublicationRequest.getSummary(), null);
        return dao.addPublication(publication);
    }

    @Override
    public User addUser(AddUserRequest addUserRequest) {
        User user = new User(null, addUserRequest.getEmail(), addUserRequest.getOrcidId(), addUserRequest.getFirstName(), addUserRequest.getMiddleName(), addUserRequest.getLastName(), addUserRequest.getOpenId());
        return dao.addUser(user);
    }

    @Override
    public Author addAuthor(AddAuthorRequest addAuthorRequest) {
        Author author = new Author(null, addAuthorRequest.getUserId(), addAuthorRequest.getOrcidId(), addAuthorRequest.getName());
        return dao.addAuthor(author);
    }

    @Override
    public GetPublicationsAPIResponse getPublicationsFilter(GetPublicationsRequest request) {
        List<Publication> publications = dao.getPublicationsFilter(request.getFilters());
        return publicationToPublicationValue(publications);
    }

    @Override
    public GetPublicationsAPIResponse getPublicationsNew(GetPublicationsRequest request) {
        List<Publication> publications = dao.getPublicationsNew(request);
        if (!isNull(request.getSearchKeywords()))
            publications.sort((publication1, publication2) -> keywordSearchComparator(publication1, publication2, request.getSearchKeywords()));
        return publicationToPublicationValue(publications);
    }

    @Override
    public UserValues getUsersByOpenId(String openId) {
        User user = dao.getUserByOpenId(openId);
        return new UserValues(user.getEmail(), user.getOrcidId(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getOpenId());
    }

    private static int keywordSearchComparator(Publication publication1, Publication publication2, Set<String> keywords) {
        return keywordSearchWeightFunction(publication2, keywords) - keywordSearchWeightFunction(publication1, keywords);
    }

    private static int keywordSearchWeightFunction(Publication publication, Set<String> keywords) {
        return keywords.stream()
                .map(keyword -> {
                    int count = 0;
                    int fromIndex = 0;
                    while ((fromIndex = publication.getTitle().indexOf(keyword, fromIndex)) != -1) {
                        count++;
                        fromIndex++;
                    }
                    fromIndex = 0;
                    while ((fromIndex = publication.getSummary().indexOf(keyword, fromIndex)) != -1) {
                        count++;
                        fromIndex++;
                    }
                    return count;
                })
                .reduce(0, (sum, count) -> sum += count);
    }

    private GetPublicationsAPIResponse publicationToPublicationValue(List<Publication> publications) {
        List<PublicationValues> publicationValues = publications.stream().map(publication -> {
            Set<String> authorNames = publication.getAuthors().stream().map(Author::getName).collect(Collectors.toSet());
            return new PublicationValues(publication.getTitle(), publication.getDoi(), publication.getUrl(), publication.getPublicationDate(), publication.getSummary(), authorNames);
        }).collect(Collectors.toList());
        return new GetPublicationsAPIResponse(publicationValues);
    }
}
