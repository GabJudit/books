package com.example.demo.controllers;

import com.example.demo.domain.entities.AuthorEntity;
import com.example.demo.service.impl.AuthorServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;
    @Autowired
    private AuthorServiceImpl authorServiceImpl;

    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        authorEntity.setId(null);
        String authorJson = objectMapper.writeValueAsString(authorEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated()


        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        authorEntity.setId(null);
        String authorJson = objectMapper.writeValueAsString(authorEntity);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/authors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber()
                ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Abigel"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(80));
    }

    @Test
    public void testThatAllAuthorsFound() {
        TestDataUtil.createAuthorA();
        TestDataUtil.createAuthorB();
        ArrayList<AuthorEntity> authorEntityIterable = this.authorServiceImpl.getAuthors();
        assertNotNull(authorEntityIterable);
    }

    @Test
    public void testThatListAuthorSuccessfullyReturnsHttp200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorSuccessfullyReturnsListOfAuthors() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        authorServiceImpl.saveAuthor(authorEntity);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/authors")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(authorEntity.getId())
                ).andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(authorEntity.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(authorEntity.getAge()));

    }

    @Test
    public void testThatAuthorWithIdNotFound() throws Exception {
       mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/10")

        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatAuthorWithWrongIdReturnBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/bb")

        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testThatAuthorWithWrongRequestReturnBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/author/1")

        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatAuthorFullUpdateSuccess() throws Exception {
        AuthorEntity author= authorServiceImpl.getAuthorById(1L).orElse(null);
        if(author!=null) {
            author.setName("NewName");
            author.setAge(90);
            String authorJson = objectMapper.writeValueAsString(author);
            mockMvc.perform(
                    MockMvcRequestBuilders.put("/authors/1")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(authorJson)
            ).andExpect(MockMvcResultMatchers.status().isNoContent());
            AuthorEntity updatedAuthor= authorServiceImpl.getAuthorById(1L).orElse(null);
            assertNotNull(updatedAuthor);
            assertEquals("NewName",updatedAuthor.getName());
            assertEquals(90,updatedAuthor.getAge());
        }

    }

      @Test
    public void testThatAuthorDeleteSuccess() throws Exception {
        AuthorEntity author= authorServiceImpl.getAuthorById(1L).orElse(null);
        if(author!=null) {
            mockMvc.perform(
                    MockMvcRequestBuilders.delete("/authors/1")
            ).andExpect(MockMvcResultMatchers.status().isNoContent());
            Optional<AuthorEntity> updatedAuthor= authorServiceImpl.getAuthorById(1L);
            assertFalse(updatedAuthor.isPresent());
        }
    }
}
