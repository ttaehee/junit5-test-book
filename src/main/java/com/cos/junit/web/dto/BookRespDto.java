package com.cos.junit.web.dto;

import com.cos.junit.domain.Book;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BookRespDto {
	
    private Long id;
    private String title;
    private String author;

    @Builder
    public BookRespDto toDto(Book bookPS) {
        this.id = bookPS.getId();
        this.title = bookPS.getTitle();
        this.author = bookPS.getAuthor();
        return this;
    }

}
