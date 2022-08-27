package com.cos.junit.web.dto;

import com.cos.junit.domain.Book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSaveReqDto {
    private String title;
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }
}