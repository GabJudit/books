package com.example.demo.service;

import com.example.demo.domain.dto.AuthorDto;
import com.example.demo.domain.entities.AuthorEntity;

import java.util.ArrayList;
import java.util.Optional;

public interface AuthorService {

    AuthorEntity saveAuthor(AuthorEntity author);
    ArrayList<AuthorEntity> getAuthors();
    Optional<AuthorEntity> getAuthorById(Long id);

    boolean isExists(Long id);

    AuthorEntity updateAuthor(Long id, AuthorDto authorDto);

    void deleteAuthorById(long id);
}
