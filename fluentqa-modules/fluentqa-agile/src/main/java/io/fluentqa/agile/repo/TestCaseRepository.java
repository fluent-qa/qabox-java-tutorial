package io.fluentqa.agile.repo;

import io.fluentqa.agile.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
}
