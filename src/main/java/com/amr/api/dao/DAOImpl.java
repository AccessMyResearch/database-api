package com.amr.api.dao;

import com.amr.api.model.Author;
import com.amr.api.model.Publication;
import com.amr.api.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DAOImpl implements DAO {
    private final PublicationRepository publicationRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;

    @Override
    public List<Publication> getPublications() {
        return publicationRepository.findAll();
    }

    @Override
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
