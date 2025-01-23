package com.example.demo.controllers;

import com.example.demo.domain.entities.BookEntity;
import com.example.demo.service.impl.BookServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final BookServiceImpl bookService;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, BookServiceImpl bookService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.bookService = bookService;
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsHttp201Created() throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA();
        String BookJson = objectMapper.writeValueAsString(bookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BookJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsSavedBook() throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA();
        String BookJson = objectMapper.writeValueAsString(bookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BookJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("1234")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.title").value("A kór"));

    }

    @Test
    public void testThatListBookSuccessfullyReturnsHttp200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListBooksSuccessfullyReturnsListOfBooks() throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA();
        bookService.saveBook("1234", bookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].isbn").value("1234")
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("A kór"));

    }

    @Test
    public void testThatListBooksSuccessfullyReturnsCreatedBook() throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA();
        bookService.saveBook("1234", bookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/1234")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("1234")
        ).andExpect(MockMvcResultMatchers.jsonPath("$.title").value("A kór"));

    }

    @Test
    public void testThatBookWithIsbnFound() throws Exception {

        BookEntity bookEntity = TestDataUtil.createBookA();
        bookService.saveBook(bookEntity.getIsbn(), bookEntity);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/books/" + bookEntity.getIsbn())

                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntity.getIsbn())
                ).andExpect(MockMvcResultMatchers.jsonPath("$.title").value(bookEntity.getTitle()));
    }

    @Test
    public void testThatBookWithIsbnNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/1111")

        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatBookPartialUpdateSuccess() throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA();
        bookService.saveBook(bookEntity.getIsbn(), bookEntity);
        BookEntity book = bookService.getBookByIsbn(bookEntity.getIsbn()).orElse(null);

        if (book != null) {
            book.setTitle("NewTitle");

            String bookJson = objectMapper.writeValueAsString(book);
            mockMvc.perform(
                    MockMvcRequestBuilders.put("/books/" + book.getIsbn())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookJson)
            );
            BookEntity updatedBook = bookService.getBookByIsbn(bookEntity.getIsbn()).orElse(null);
            assertNotNull(updatedBook);
            assertEquals("NewTitle", updatedBook.getTitle());
        }
    }

    @Test
    public void testThatBookDeleteSuccess() throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA();
        bookService.saveBook(bookEntity.getIsbn(), bookEntity);
        BookEntity book = bookService.getBookByIsbn(bookEntity.getIsbn()).orElse(null);

        if (book != null) {
            mockMvc.perform(
                    MockMvcRequestBuilders.delete("/books/" + book.getIsbn())
            ).andExpect(MockMvcResultMatchers.status().isOk());
            Optional<BookEntity> deletedBook = bookService.getBookByIsbn(bookEntity.getIsbn());
            assertFalse(deletedBook.isPresent());
        }
    }
}
