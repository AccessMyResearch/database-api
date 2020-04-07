package com.amr.api.model;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name="users")
public class User {
    @Id
    Integer id;

    String email;

    @Column(name="orcid_id")
    String orcidId;
    @Column(name="first_name")
    String firstName;
    @Column(name="middle_name")
    String middleName;
    @Column(name="last_name")
    String lastName;
}
