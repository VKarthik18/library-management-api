package com.library.librarymanagementapi.controller;

import com.library.librarymanagementapi.entity.Book;
import com.library.librarymanagementapi.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/available")
public List<Book> getAvailableBooks() {
    return bookService.getAvailableBooks();
}

    @GetMapping("/overdue")
public List<Book> getOverdueBooks() {
    return bookService.getOverdueBooks();
}

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
public List<Book> searchBooks(@RequestParam String title) {
    return bookService.searchBooksByTitle(title);
}

@GetMapping("/search/author")
public List<Book> searchBooksByAuthor(@RequestParam String author) {
    return bookService.searchBooksByAuthor(author);
}

@GetMapping("/search/isbn")
public ResponseEntity<Book> searchBookByIsbn(@RequestParam String isbn) {
    Book book = bookService.searchBookByIsbn(isbn);

    if (book == null) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(book);
}
    @PostMapping
public Book addBook(@Valid @RequestBody Book book) {
    return bookService.addBook(book);
}

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestBody Book book) {

        if (bookService.getBookById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        book.setId(id);
        return ResponseEntity.ok(bookService.updateBook(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {

        if (bookService.getBookById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}