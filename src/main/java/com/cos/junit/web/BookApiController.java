package com.cos.junit.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.junit.service.BookService;
import com.cos.junit.web.dto.request.BookSaveReqDto;
import com.cos.junit.web.dto.response.BookListRespDto;
import com.cos.junit.web.dto.response.BookRespDto;
import com.cos.junit.web.dto.response.CommonRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BookApiController {
	
	private final BookService bookService;
	
	//책등록
    @PostMapping("/api/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult) {
    	
    	//추후 AOP 처리로 변경 고려하기
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }

            throw new RuntimeException(errorMap.toString());
        }

        BookRespDto bookRespDto = bookService.insertBook(bookSaveReqDto);
        
        return new ResponseEntity<>(CommonRespDto.builder().code(1).msg("글 저장 성공").body(bookRespDto).build(),
                HttpStatus.CREATED);
    }
    
    //책목록보기
    @GetMapping("/api/v1/book")
    public ResponseEntity<?> getBookList() {
    	
        BookListRespDto bookListRespDto = bookService.selectAllBook();
        
        return new ResponseEntity<>(CommonRespDto.builder().code(1).msg("글 목록보기 성공").body(bookListRespDto).build(),
                HttpStatus.OK);
    }

}
