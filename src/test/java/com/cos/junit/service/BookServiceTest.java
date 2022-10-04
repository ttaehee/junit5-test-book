package com.cos.junit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.cos.junit.domain.Book;
import com.cos.junit.domain.BookRepository;
import com.cos.junit.util.MailSender;
import com.cos.junit.web.dto.request.BookSaveReqDto;
import com.cos.junit.web.dto.response.BookListRespDto;
import com.cos.junit.web.dto.response.BookRespDto;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
	
	@InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MailSender mailSender;
    
    @DisplayName("책 등록")
    @Test
    public void insertBook_test() {
    	
        //given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit5");
        dto.setAuthor("김태희");

        //stub(가설)
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        when(mailSender.send()).thenReturn(true);

        //when
        BookRespDto bookRespDto = bookService.insertBook(dto);

        //then
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());
    }
    
    @DisplayName("책 목록보기")
    @Test
    public void selectAllBook_test() {
    	
    	//given
    	List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "junit", "김태희"));
        books.add(new Book(2L, "spring", "태희"));
    	
    	//stub
    	when(bookRepository.findAll()).thenReturn(books);
    	
    	//when
    	BookListRespDto bookListRespDto = bookService.selectAllBook();
    	
    	//then
        assertThat(bookListRespDto.getItems().get(0).getTitle()).isEqualTo("junit");
        assertThat(bookListRespDto.getItems().get(0).getAuthor()).isEqualTo("김태희");
        assertThat(bookListRespDto.getItems().get(1).getTitle()).isEqualTo("spring");
        assertThat(bookListRespDto.getItems().get(1).getAuthor()).isEqualTo("태희");
    }  
    
    @DisplayName("책 한건보기")
    @Test
    public void selectBook_test() {
    	
    	//given
        Long id = 1L;

        //stub
        Book book = new Book(1L, "junit", "김태희");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        //when
        BookRespDto bookRespDto = bookService.selectBook(id);

        //then
        assertThat(bookRespDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(book.getAuthor());	
    }
    
    @DisplayName("책 수정")
    @Test
    public void updateBook_test() {
    	
    	//given
        Long id = 1L;
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("spring");
        dto.setAuthor("태희");

        //stub
        Book book = new Book(1L, "junit", "김태희");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        //when
        BookRespDto bookRespDto = bookService.updateBook(id, dto);

        //then
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());	
    }   

}
