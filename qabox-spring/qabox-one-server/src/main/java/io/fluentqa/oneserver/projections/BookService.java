package io.fluentqa.oneserver.projections;

import java.util.List;


public interface BookService {

    List<Book> getBooks();

    Book getBook(Long bookId);

    Book saveOrUpdate(Book book);

}