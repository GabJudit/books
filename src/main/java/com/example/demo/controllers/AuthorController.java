package com.example.demo.controllers;

import com.example.demo.domain.dto.AuthorDto;
import com.example.demo.domain.entities.AuthorEntity;
import com.example.demo.mappers.Mapper;
import com.example.demo.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    private final AuthorService authorService;
    private final Mapper<AuthorEntity, AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        AuthorEntity authorEntity = authorService.saveAuthor(authorMapper.mapFrom(author));
        return new ResponseEntity<>(authorMapper.mapTo(authorEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/authors")
    public List<AuthorDto> getAuthors() {
        ArrayList<AuthorEntity> authorEntityList = authorService.getAuthors();
        return authorEntityList.stream().map(authorMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable long id) {
        AuthorEntity authorEntity = authorService.getAuthorById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        AuthorDto authorDto = authorMapper.mapTo(authorEntity);
        return ResponseEntity.ok(authorDto);
    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable long id, @RequestBody AuthorDto author) {
        AuthorEntity authorEntity = authorService.getAuthorById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author entity not found: "+ id));
        AuthorEntity mapped=authorMapper.mapFrom(author);
        authorService.saveAuthor(mapped);
        Optional<AuthorEntity> savedAuthorEntity = authorService.getAuthorById(id);
        return savedAuthorEntity.map(entity -> new ResponseEntity<>(authorMapper.mapTo(entity), HttpStatus.OK)).orElse(null);
    }


    @DeleteMapping(path = "/authors/{id}")
    public void deleteAuthorById(@PathVariable long id) {
        authorService.deleteAuthorById(id);
    }
}
