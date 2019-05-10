package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@Api(value = "Books", description = "REST API for Books", tags = { "Books" })
public class BookController {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    @Autowired
    public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping("/books")
    @PreAuthorize("hasAuthority('USER')")
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @GetMapping("/books/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public Book getBookById(@PathVariable(name = "id") int id){

        return bookRepository.getOne(id);
    }

    @DeleteMapping("/books/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteBookById(@PathVariable(name = "id") int id){

        bookRepository.deleteById(id);
    }

    @PostMapping("/books")
    @PreAuthorize("hasAuthority('ADMIN')")
    public  void saveBook(@RequestBody Book book){
        bookRepository.save(book);
    }

    @GetMapping("/booksByAuthor/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public List<Book> getBooksByAuthor(@PathVariable(name = "id") int id){

        return bookRepository.findAllByAuthor(authorRepository.getOne(id));
    }
}
