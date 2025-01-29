package com.example.demo.service.impl;

import com.example.demo.domain.dto.AuthorDto;
import com.example.demo.domain.dto.BookDto;
import com.example.demo.domain.entities.AuthorEntity;
import com.example.demo.domain.entities.BookEntity;
import com.example.demo.mappers.AuthorMapper;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BookRepository;
import com.example.demo.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public BookServiceImpl(BookRepository bookRepository,AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public BookEntity saveBook(String isbn,BookEntity book) {
        book.setIsbn(isbn);
        return bookRepository.save(book);
    }

    @Override
    public ArrayList<BookEntity> getBooks() {
        ArrayList<BookEntity> books= new ArrayList<>() {};
        Sort sort = Sort.by(Sort.Direction.DESC, "title");
        Pageable pageable = PageRequest.of(0, 4, sort);
        bookRepository.findAll(sort).forEach(books::add);
        return books;
    }

    @Override
    public Optional<BookEntity> getBookByIsbn(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public boolean isExists(String isbn) {
        return bookRepository.existsById(isbn);
    }

    @Override
    public BookEntity updateBook(String isbn, BookDto book) {

        Optional<BookEntity> bookOptional = bookRepository.findById(isbn);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Book not found: "+isbn);
        }

        BookEntity bookFounded = bookOptional.get();
        bookFounded.setIsbn(isbn);
        bookFounded.setTitle(book.getTitle());
        bookFounded.setAuthor(authorMapper.mapFrom(book.getAuthor()));

        return bookRepository.save(bookFounded);

    }

    @Override
    public BookEntity addAuthorToBook(String isbn, Long authorId){
        Optional<BookEntity> bookOptional = bookRepository.findById(isbn);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Book not found: "+isbn);
        }

        BookEntity bookFounded = bookOptional.get();
        Optional<AuthorEntity> authorOptional =authorRepository.findById(authorId);
        if(authorOptional.isEmpty()){
            throw new EntityNotFoundException("Author not found: "+authorId);
        }else{
            bookFounded.setAuthor(authorOptional.get());
        }
        return bookRepository.save(bookFounded);
    }

    @Override
    public void deleteBookByIsbn(String isbn) {
        bookRepository.deleteById(isbn);
    }

}
