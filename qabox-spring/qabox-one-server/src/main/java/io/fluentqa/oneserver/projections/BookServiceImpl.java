package io.fluentqa.oneserver.projections;

import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookRepo;

    public BookServiceImpl(BookDao bookRepo) {
	this.bookRepo = bookRepo;
    }

    @Override
    public List<Book> getBooks() {
	return bookRepo.findAll();
    }

    @Override
    public Book getBook(Long bookId) {
	return bookRepo.getReferenceById(bookId);
    }

    @Override
    public Book saveOrUpdate(Book book) {
	return bookRepo.save(book);
    }

}