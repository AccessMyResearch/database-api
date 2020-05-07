package com.amr.api.dao;

import com.amr.api.model.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface DAO {
    List<Publication> getPublications();

    List<Publication> getPublicationsNew(GetPublicationsRequest getPublicationsRequest);

    List<Author> getAuthors();
    List<User> getUsers();

    default Publication addPublication(Publication publication) {
        return addPublication(publication, HandleDuplicateBehavior.ERROR);
    }
    default Publication addPublication(Publication publication, HandleDuplicateBehavior duplicateBehavior) {
        return addPublications(Collections.singleton(publication), duplicateBehavior).get(0);
    }
    default List<Publication> addPublications(Collection<Publication> publications) {
        return addPublications(publications, HandleDuplicateBehavior.ERROR);
    }
    List<Publication> addPublications(Collection<Publication> publications, HandleDuplicateBehavior duplicateBehavior);

    default Author addAuthor(Author author) {
        return addAuthor(author, HandleDuplicateBehavior.ERROR);
    }
    default Author addAuthor(Author author, HandleDuplicateBehavior duplicateBehavior) {
        return addAuthors(Collections.singleton(author), duplicateBehavior).get(0);
    }
    default List<Author> addAuthors(Collection<Author> authors) {
        return addAuthors(authors, HandleDuplicateBehavior.ERROR);
    }
    List<Author> addAuthors(Collection<Author> authors, HandleDuplicateBehavior duplicateBehavior);

    User addUser(User user);

    List<Publication> getPublicationsByAuthor(String authorName);

    List<Publication> getPublicationsRecent();

    List<Publication> getPublicationsByYear(Integer startYear, Integer endYear);

    List<Publication> getPublicationsFilter(FilterList filters);

    User getUserByOpenId(String openId);
}
