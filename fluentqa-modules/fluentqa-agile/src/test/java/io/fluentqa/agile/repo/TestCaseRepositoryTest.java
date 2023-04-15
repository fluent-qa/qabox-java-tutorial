package io.fluentqa.agile.repo;

import io.fluentqa.agile.entity.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith({SpringExtension.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestCaseRepositoryTest {
  @Autowired
  TestCaseRepository testCaseRepository;

  @Test
  public void testInitTestCaseRepository(){
    assertNotNull(testCaseRepository);
  }

  @Test
  public void testSaveTestCase(){
    TestCase tc = new TestCase();
    tc.setPrecondition("Pre-conditions");
    tc.setSteps("Steps");
    tc.setExpectedResult("ExpectedResult");
    testCaseRepository.save(tc);
    System.out.println(tc);
    assertEquals(tc.getId(),testCaseRepository.findById(tc.getId()).get().getId());
  }


}