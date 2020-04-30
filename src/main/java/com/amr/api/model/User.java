package com.amr.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name="orcid_id", length = 16, unique = true)
    String orcidId;

    @Column(name="first_name", length = 50, nullable = false)
    String firstName;

    @Column(name="middle_name", length = 50)
    String middleName;

    @Column(name="last_name", length = 50, nullable = false)
    String lastName;

    @Column(name="open_id", length = 36, nullable = false, unique = true)
    String openId;
}
