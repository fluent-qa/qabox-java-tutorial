package io.fluentqa.agile.repo.relationship;

import io.fluentqa.agile.entity.relationship.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
  long countByCity(String city);
  List<Author> removeByCity(String city);
  List<Author> findByCity(String city);

}
