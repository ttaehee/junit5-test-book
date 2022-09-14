package com.cos.junit.web.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BookListRespDto {
	
    List<BookRespDto> bookList;

    @Builder
    public BookListRespDto(List<BookRespDto> bookList) {
        this.bookList = bookList;
    }
}
