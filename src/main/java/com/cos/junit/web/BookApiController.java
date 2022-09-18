package com.cos.junit.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
                HttpStatus.CREATED);  //201 = insert
    }
    
    //책목록보기
    @GetMapping("/api/v1/book")
    public ResponseEntity<?> getBookList() {
    	
        BookListRespDto bookListRespDto = bookService.selectAllBook();
        
        return new ResponseEntity<>(CommonRespDto.builder().code(1).msg("글 목록보기 성공").body(bookListRespDto).build(),
                HttpStatus.OK);  //200 = ok
    }
    
    //책한건보기
    @GetMapping("/api/v1/book/{id}")
    public ResponseEntity<?> getBookOne(@PathVariable Long id) {
    	
        BookRespDto bookRespDto = bookService.selectBook(id);
        
        return new ResponseEntity<>(CommonRespDto.builder().code(1).msg("글 한건보기 성공").body(bookRespDto).build(),
                HttpStatus.OK); // 200 = ok
    }
    
    @DeleteMapping("/api/v1/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
    	
        bookService.deleteBook(id);
        
        return new ResponseEntity<>(CommonRespDto.builder().code(1).msg("글 삭제하기 성공").body(null).build(),
                HttpStatus.OK); // 200 = ok
    }

}
