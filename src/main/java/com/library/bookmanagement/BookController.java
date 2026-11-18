package com.library.bookmanagement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private List<Book> books = new ArrayList<>();

    private Book findBookById(int id) {
        for (Book b : books) {
            if (b.getBookId() == id) {
                return b;
            }
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        if (findBookById(book.getBookId()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Book with this ID already exists");
        }
        if (book.getAvailableCopies() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Available copies cannot be negative");
        }
        books.add(book);
        return ResponseEntity.status(HttpStatus.CREATED).body("Book added successfully");
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return books;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable int id) {
        Book book = findBookById(id);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}/issue")
    public ResponseEntity<String> issueBook(@PathVariable int id) {
        Book book = findBookById(id);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
        if (book.getAvailableCopies() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book not available");
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        return ResponseEntity.ok("Book issued successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id) {
        Book book = findBookById(id);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
        books.remove(book);
        return ResponseEntity.ok("Book deleted successfully");
    }
}