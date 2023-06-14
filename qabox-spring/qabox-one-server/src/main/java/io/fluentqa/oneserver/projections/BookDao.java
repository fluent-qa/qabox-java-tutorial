package io.fluentqa.oneserver.projections;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookDao extends JpaRepository<Book, Long> {

}