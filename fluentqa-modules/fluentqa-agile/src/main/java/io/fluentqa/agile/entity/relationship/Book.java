package io.fluentqa.agile.entity.relationship;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable(name = "book_author_xref",
           joinColumns = { @JoinColumn(name = "book_id") },
           inverseJoinColumns = { @JoinColumn(name = "author_id") })
    private Set authors = new HashSet();

}