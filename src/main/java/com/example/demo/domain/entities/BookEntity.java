package com.example.demo.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "book")
public class BookEntity {

    @Id
    private String isbn;
    private String title;
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="author_id")
    private AuthorEntity author;

}
