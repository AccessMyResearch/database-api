package com.amr.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "user_id", unique = true)
    Integer userId;

    @Column(name = "orcid_id", length = 16, unique = true)
    String orcidId;

    @Column(name = "name", length = 100)
    String name;
}
