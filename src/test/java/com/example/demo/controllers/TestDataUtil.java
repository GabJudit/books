package com.example.demo.controllers;

import com.example.demo.domain.dto.AuthorDto;
import com.example.demo.domain.entities.AuthorEntity;
import com.example.demo.domain.entities.BookEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDataUtil {

    public static AuthorEntity createAuthorA(){
        AuthorEntity authorEntity= new AuthorEntity() ;
        authorEntity.setId(1L);
        authorEntity.setName("Abigel");
        authorEntity.setAge(80);
        return authorEntity;
    }

    public static AuthorEntity createAuthorB(){
        AuthorEntity authorEntity= new AuthorEntity() ;
        authorEntity.setId(2L);
        authorEntity.setName("John");
        authorEntity.setAge(20);
        return authorEntity;
    }

    public static BookEntity createBookA(){
        AuthorEntity author=createAuthorA();
        BookEntity bookEntity= new BookEntity() ;
        bookEntity.setIsbn("1234");
        bookEntity.setTitle("A kór");
        bookEntity.setAuthor(author);
        return bookEntity;
    }

    public static BookEntity createBookB(){
        AuthorEntity author=createAuthorB();
        BookEntity bookEntity= new BookEntity() ;
        bookEntity.setIsbn("fg89");
        bookEntity.setTitle("láb");
        bookEntity.setAuthor(author);
        return bookEntity;
    }

    public static BookEntity createBookWithoutAuthor(){
         BookEntity bookEntity= new BookEntity() ;
        bookEntity.setIsbn("aaaa");
        bookEntity.setTitle("A kórlap");
         return bookEntity;
    }

}
