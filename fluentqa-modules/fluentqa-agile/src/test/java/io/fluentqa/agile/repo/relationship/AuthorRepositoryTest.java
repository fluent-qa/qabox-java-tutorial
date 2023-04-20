package io.fluentqa.agile.repo.relationship;

import io.fluentqa.agile.entity.relationship.Address;
import io.fluentqa.agile.entity.relationship.Author;
import io.fluentqa.agile.entity.relationship.Book;
import org.assertj.core.util.Sets;
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
public class AuthorRepositoryTest {
  @Autowired
  private BookRepository bookRepository;
  @Autowired
  private AuthorRepository authorRepository;

  @Test
  public void testBookRepository(){
    Book book = new Book();
    Author author = new Author();
    Address address = new Address();
    address.setCity("shagnhai");
    address.setStreet("street");
    address.setPostalCode("21000");

    author.setAddress(address);
    author.setBusinessAddress(address);
//    author.setBooks(Sets.newTreeSet(book));
    book.setAuthors(Sets.newTreeSet(author));

    bookRepository.save(book);
    authorRepository.save(author);

  }
}