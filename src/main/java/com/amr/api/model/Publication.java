package com.amr.api.model;

import lombok.Getter;
import lombok.Value;

import javax.persistence.*;
import java.util.Set;

@Getter
@Entity
@Table(name="publications")
public class Publication {
    @Id
    Integer id;

    String title;
    String doi;
    String url;

    @Column(name="publication_date")
    String publicationDate;

    @Column(name="abstract")
    String summary;

    @ManyToMany
    @JoinTable(
            name = "publications_authors",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    Set<Author> authors;
}
