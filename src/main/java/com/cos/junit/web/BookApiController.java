package com.cos.junit.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.junit.service.BookService;
import com.cos.junit.web.dto.request.BookSaveReqDto;
import com.cos.junit.web.dto.response.BookRespDto;
import com.cos.junit.web.dto.response.CommonRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BookApiController {
	
	private final BookService bookService;
	
	//책등록
    @PostMapping("/api/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody BookSaveReqDto bookSaveReqDto) {

        BookRespDto bookRespDto = bookService.insertBook(bookSaveReqDto);
        
        return new ResponseEntity<>(CommonRespDto.builder().code(1).msg("글 저장 성공").body(bookRespDto).build(),
                HttpStatus.CREATED);
    }

}
