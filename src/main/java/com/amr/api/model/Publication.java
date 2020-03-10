package com.amr.api.model;

import lombok.Getter;
import lombok.Value;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name="Articles")
public class Publication {
    @Id
    String id;

    String title;
    String doi;
    String url;
}
