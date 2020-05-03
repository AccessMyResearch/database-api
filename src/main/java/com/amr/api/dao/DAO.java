package com.amr.api.dao;

import com.amr.api.model.*;

import java.util.List;

public interface DAO {
    List<Publication> getPublications();

    List<Publication> getPublicationsNew(GetPublicationsRequest getPublicationsRequest);

    List<Author> getAuthors();
    List<User> getUsers();

    Publication addPublication(Publication publication);
    List<Publication> addPublications(Iterable<Publication> publication);
    User addUser(User user);
    Author addAuthor(Author author);

    List<Publication> getPublicationsByAuthor(String authorName);

    List<Publication> getPublicationsRecent();

    List<Publication> getPublicationsByYear(Integer startYear, Integer endYear);

    List<Publication> getPublicationsFilter(FilterList filters);

    User getUserByOpenId(String openId);
}
