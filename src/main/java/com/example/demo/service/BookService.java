package com.example.demo.service;

import com.example.demo.domain.dto.BookDto;
import com.example.demo.domain.entities.BookEntity;

import java.util.ArrayList;
import java.util.Optional;

public interface BookService {

    BookEntity saveBook(String isbn,BookEntity book);
    ArrayList<BookEntity> getBooks();
    Optional<BookEntity> getBookByIsbn(String isbn);
    boolean isExists(String isbn);

    BookEntity updateBook(String isbn, BookDto book);

    void deleteBookByIsbn(String isbn);
}
