package com.cos.junit.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.junit.domain.Book;
import com.cos.junit.domain.BookRepository;
import com.cos.junit.util.MailSender;
import com.cos.junit.web.dto.request.BookSaveReqDto;
import com.cos.junit.web.dto.response.BookListRespDto;
import com.cos.junit.web.dto.response.BookRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BookService {
	
	private final BookRepository bookRepository;
	private final MailSender mailSender;
	
	//책 등록
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto insertBook(BookSaveReqDto dto) {
    	
        Book bookPS = bookRepository.save(dto.toEntity());
        if (bookPS != null) {
            if (!mailSender.send()) {
                throw new RuntimeException("메일이 전송되지 않았습니다");
            }
        }
        return bookPS.toDto();
    }
    
    //책 목록보기
    public BookListRespDto selectAllBook(){
    	
        List<BookRespDto> dtos = bookRepository.findAll().stream()
              //.map((bookPS) -> bookPS.toDto())
                .map(Book::toDto)
                .collect(Collectors.toList());

        BookListRespDto bookListRespDto = BookListRespDto.builder().bookList(dtos).build();
        return bookListRespDto;
    }
    
    //책 한건보기
    public BookRespDto selectBook(Long id) {
    	
    	Optional<Book> bookOP = bookRepository.findById(id);
    	if(bookOP.isPresent()) {
    		return bookOP.get().toDto();
    	}else {
    		throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
    	}  
    }
    
    //책 삭제하기
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteBook(Long id) {
    	bookRepository.deleteById(id);
    }
    
    //책 수정
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto updateBook(Long id, BookSaveReqDto dto) {
    	
    	Optional<Book> bookOP = bookRepository.findById(id);
    	if(bookOP.isPresent()) {
    		Book bookPS = bookOP.get();
    		bookPS.update(dto.getTitle(), dto.getAuthor());
    		return bookPS.toDto();
    	}else {
    		throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
    	}
    }
  
}
