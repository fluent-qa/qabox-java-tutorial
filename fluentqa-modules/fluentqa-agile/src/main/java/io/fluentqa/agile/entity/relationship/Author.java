package io.fluentqa.agile.entity.relationship;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

//  @ManyToMany(mappedBy = "authors")
//  private Set<Book> books = new HashSet<Book>();

  @Embedded
  private Address address;

  @Embedded
  @AttributeOverride(
    name = "street",
    column = @Column( name = "business_street" )
  )
  @AttributeOverride(
    name = "city",
    column = @Column( name = "business_city" )
  )
  @AttributeOverride(
    name = "postalCode",
    column = @Column( name = "business_postcalcode" )
  )
  private Address businessAddress;
}