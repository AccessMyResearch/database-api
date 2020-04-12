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

    @Override
    public boolean addPublication(Publication publication){
        publicationRepository.save(publication);
        return true;
    }

    @Override
    public boolean addUser(User user) {
        userRepository.save(user);
        return true;
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
        String startDate = startYear.toString().concat("-01-01");
        String endDate = endYear.toString().concat("-12-31");
        return publicationRepository.findAllByPublicationDateBetween(startDate, endDate);
    }
}
