package com.amr.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="publications")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NonNull
    @Column(name = "title", length = 500, nullable = false)
    String title;

    @NonNull
    @Column(name = "doi", length = 100, nullable = false, unique = true)
    String doi;

    @Column(name = "url")
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
