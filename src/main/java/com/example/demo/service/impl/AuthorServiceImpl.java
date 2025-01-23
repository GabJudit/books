package com.example.demo.service.impl;

import com.example.demo.domain.dto.AuthorDto;
import com.example.demo.domain.entities.AuthorEntity;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity saveAuthor(AuthorEntity author) {
        return authorRepository.save(author);
    }

    @Override
    public ArrayList<AuthorEntity> getAuthors() {
        ArrayList<AuthorEntity> authors= new ArrayList<>() {};
        Sort sort = Sort.by(Sort.Direction.DESC, "name");
        Pageable pageable = PageRequest.of(0, 4, sort);
        authorRepository.findAll(pageable).forEach(authors::add);
       return authors;
    }
    @Override
    public Optional<AuthorEntity> getAuthorById(Long id){
        return authorRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return authorRepository.existsById(id);
    }



    @Override
    public AuthorEntity updateAuthor(Long id, AuthorDto authorDto){
        return authorRepository.findById(id).map(author -> {
            author.setName(authorDto.getName());
            author.setAge(authorDto.getAge());
            return authorRepository.save(author);
        }).orElseThrow(() -> new EntityNotFoundException("Author entity not found: "+ id));
    }

    @Override
    public void deleteAuthorById(long id) {
        authorRepository.deleteById(id);
    }
}
