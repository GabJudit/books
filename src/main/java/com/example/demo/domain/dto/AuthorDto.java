package com.example.demo.domain.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AuthorDto {

    private Long id;
    private String name;
    private Integer age;
}
