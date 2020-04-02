package com.amr.api.model;

import lombok.Getter;
import lombok.Value;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name="publications")
public class Publication {
    @Id
    int id;

    String title;
    String doi;
    String url;

    @Column(name="publication_date")
    String publicationDate;

    @Column(name="abstract")
    String summary;
}
