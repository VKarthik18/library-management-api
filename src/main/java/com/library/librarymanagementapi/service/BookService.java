package com.library.librarymanagementapi.service;

import com.library.librarymanagementapi.entity.Issue;
import com.library.librarymanagementapi.repository.IssueRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import com.library.librarymanagementapi.entity.Book;
import com.library.librarymanagementapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final IssueRepository issueRepository;

    public BookService(BookRepository bookRepository,
                   IssueRepository issueRepository) {
    this.bookRepository = bookRepository;
    this.issueRepository = issueRepository;
}

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAvailableBooks() {
    return bookRepository.findByAvailableTrue();
    }

    public List<Book> getOverdueBooks() {

    List<Issue> overdueIssues =
            issueRepository.findByReturnedFalseAndDueDateBefore(LocalDate.now());

    List<Book> overdueBooks = new ArrayList<>();

    for (Issue issue : overdueIssues) {
        bookRepository.findById(issue.getBookId())
                .ifPresent(overdueBooks::add);
    }

    return overdueBooks;
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> searchBooksByTitle(String title) {
    return bookRepository.findByTitleContainingIgnoreCase(title);
}

   public List<Book> searchBooksByAuthor(String author) {
    return bookRepository.findByAuthorContainingIgnoreCase(author);
}

public Book searchBookByIsbn(String isbn) {
    return bookRepository.findByIsbn(isbn);
}

    public Book addBook(Book book) {
    book.setAvailable(true);
    return bookRepository.save(book);
}

    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}