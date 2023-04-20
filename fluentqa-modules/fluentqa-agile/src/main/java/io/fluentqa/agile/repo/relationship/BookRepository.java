package io.fluentqa.agile.repo.relationship;

import io.fluentqa.agile.entity.relationship.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
