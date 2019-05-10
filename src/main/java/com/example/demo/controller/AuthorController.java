package com.example.demo.controller;


import com.example.demo.model.Author;

import com.example.demo.repository.AuthorRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "Authors", description = "REST API for Authors", tags = { "Authors" })
public class AuthorController {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping("/authors")
    @PreAuthorize("hasAuthority('USER')")
    public List<Author> getAllAuthors(){
        return authorRepository.findAll();
    }

    @GetMapping("/authors/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public Author getAuthorById(@PathVariable(name = "id") int id){

        return authorRepository.getOne(id);
    }

    @DeleteMapping("/authors/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAuthorById(@PathVariable(name = "id") int id){

        authorRepository.deleteById(id);
    }

    @PostMapping("/authors")
    @PreAuthorize("hasAuthority('ADMIN')")
    public  void saveAuthor(@RequestBody Author author){
        authorRepository.save(author);
    }

}
