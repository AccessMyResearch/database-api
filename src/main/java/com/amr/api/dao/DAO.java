package com.amr.api.dao;

import com.amr.api.model.Author;
import com.amr.api.model.Publication;
import com.amr.api.model.User;

import java.util.List;

public interface DAO {
    List<Publication> getPublications();
    List<Author> getAuthors();
    List<User> getUsers();
}
