package com.amr.api.service;

import com.amr.api.dao.DAO;
import com.amr.api.dao.DAOImpl;
import com.amr.api.dao.PublicationRepository;
import com.amr.api.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceImpl implements com.amr.api.service.Service {
    private final DAO dao;

    @Override
    public GetPublicationsAPIResponse getPublications() {
        List<Publication> publications = dao.getPublications();
        List<PublicationValues> publicationValues = publications.stream().map(publication -> new PublicationValues(publication.getTitle(), publication.getDoi(), publication.getUrl())).collect(Collectors.toList());
        return new GetPublicationsAPIResponse(publicationValues);
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
        List<UserValues> userValues = users.stream().map(user -> new UserValues(user.getEmail(), user.getOrcidId(), user.getFirstName(), user.getMiddleName(), user.getLastName())).collect(Collectors.toList());
        return new GetUsersAPIResponse(userValues);
    }
}
