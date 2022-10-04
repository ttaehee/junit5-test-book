package com.cos.junit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("dev")
@DataJpaTest
public class BookRepositoryTest {
	
	@Autowired
	private BookRepository bookRepository;
	
    @BeforeEach //각 테스트 시작전 한번씩 실행
    public void getData() {
        String title = "junit";
        String author = "태희";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();
        bookRepository.save(book);
    }
	
	@DisplayName("책 등록")
	@Test
    public void insert_test() {
		
        //given (데이터 준비)
        String title = "junit5";
        String author = "김태희";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        //when(테스트 실행)
        Book bookPS = bookRepository.save(book);

        //then(검증)
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
        
    } //트랜잭션 종료(저장된 데이터를 초기화함)
	
	
	@DisplayName("책 목록")
	@Test
	public void selectAll_test() {
		
        //given
		String title = "junit";
        String author = "태희";
				
		//when
		List<Book> books = bookRepository.findAll();
		
		//then
		assertEquals(title, books.get(0).getTitle());
        assertEquals(author, books.get(0).getAuthor());
		
	}
	
	@DisplayName("책 한건보기")
	@Sql("classpath:db/tableInit.sql")
    @Test
    public void select_test() {
    	
        //given
        String title = "junit";
        String author = "태희";

        //when
        Book bookPS = bookRepository.findById(1L).get();

        //then
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
        
    }
    
	@DisplayName("책 삭제")
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void delete_test() {
    	
    	//given
    	Long id = 1L;
    	
    	//when
    	bookRepository.deleteById(id);
    	
    	//then
    	assertFalse(bookRepository.findById(id).isPresent());
    	
    }
    
	@DisplayName("책 수정")
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void update_test() {
    	
    	//given
    	Long id = 1L;
    	String title = "transaction";
    	String author = "Kimtaehee";
    	Book book = new Book(id, title, author);
    	
    	//when
    	Book bookPS = bookRepository.save(book);
    	
    	//then
    	assertEquals(id, bookPS.getId());
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    	
    }

}
