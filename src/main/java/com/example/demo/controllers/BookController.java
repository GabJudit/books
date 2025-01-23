package com.example.demo.controllers;

import com.example.demo.domain.dto.AuthorDto;
import com.example.demo.domain.dto.BookDto;
import com.example.demo.domain.entities.AuthorEntity;
import com.example.demo.domain.entities.BookEntity;
import com.example.demo.mappers.Mapper;
import com.example.demo.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {
    private final BookService bookService;
    private final Mapper<BookEntity, BookDto> bookMapper;


    public BookController(BookService bookService,Mapper<BookEntity,BookDto> bookMapper) {
        this.bookService = bookService;
        this.bookMapper=bookMapper;
    }

    @PostMapping(path="/books/{isbn}")
    public ResponseEntity<BookDto> createBook(@PathVariable("isbn") String isbn, @RequestBody BookDto book){
        BookEntity bookEntity=bookService.saveBook(isbn, bookMapper.mapFrom(book));
        return new ResponseEntity<>(bookMapper.mapTo(bookEntity), HttpStatus.CREATED);
    }

    @GetMapping(path="/books")
    public List<BookDto> getBooks(){
        ArrayList<BookEntity> bookEntityList=bookService.getBooks();
        return bookEntityList.stream().map(bookMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path="/books/{isbn}")
    public ResponseEntity<BookDto> getBookByIsbn(@PathVariable String isbn){
   BookEntity bookEntity= bookService.getBookByIsbn(isbn)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        BookDto bookDto = bookMapper.mapTo(bookEntity);
        return ResponseEntity.ok(bookDto);
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> updateBook(@PathVariable String isbn, @RequestBody BookDto book) {
        BookEntity bookEntity = bookService.getBookByIsbn(isbn)
                .orElseThrow(() -> new EntityNotFoundException("Book Entity not found: "+isbn));
        BookEntity mapped =bookMapper.mapFrom(book);
        bookService.saveBook(isbn,mapped);
        Optional<BookEntity> savedBookEntity = bookService.getBookByIsbn(isbn);
        return savedBookEntity.map(entity -> new ResponseEntity<>(bookMapper.mapTo(entity), HttpStatus.OK)).orElse(null);
    }


    @DeleteMapping(path="/books/{isbn}")
    public void deleteBookByIsbn(@PathVariable String isbn){
        bookService.deleteBookByIsbn(isbn);
            }
}
