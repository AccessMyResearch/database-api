package com.amr.api.model;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name="authors")
public class Author {
    @Id
    int id;

    @Column(name="user_id")
    int userId;
    @Column(name="orcid_id")
    String orcidId;
    String name;
}
