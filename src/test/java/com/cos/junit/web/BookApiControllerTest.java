package com.cos.junit.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import com.cos.junit.domain.Book;
import com.cos.junit.domain.BookRepository;
import com.cos.junit.web.dto.request.BookSaveReqDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private TestRestTemplate rt;
	
	private static ObjectMapper om;
	private static HttpHeaders headers;
	
	@BeforeAll
	public static void init() {
		om = new ObjectMapper();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}
	
	@BeforeEach
    public void getData() {
        String title = "junit";
        String author = "태희";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();
        bookRepository.save(book);
    }
	
	@Test
	@DisplayName("책 등록")
	public void saveBook_test() throws JsonProcessingException {
		
		//given
		BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
		bookSaveReqDto.setTitle("스프링1강");
		bookSaveReqDto.setAuthor("태희");
		
		String body = om.writeValueAsString(bookSaveReqDto);
		
		//when
		HttpEntity<String> request = new HttpEntity<>(body, headers);	
		ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.POST, request, String.class);
		
		//then
		DocumentContext dc = JsonPath.parse(response.getBody());
		String title = dc.read("$.body.title");
		String author = dc.read("$.body.author");
		
		assertThat(title).isEqualTo("스프링1강");
		assertThat(author).isEqualTo("태희");
		
	}
	
	@Sql("classpath:db/tableInit.sql")
	@Test
	@DisplayName("책 목록")
	public void getBookList_test() {
		
		//given
		
		//when
		HttpEntity<String> request = new HttpEntity<>(null, headers);	
		ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.GET, request, String.class);
		
		//then
		DocumentContext dc = JsonPath.parse(response.getBody());
		Integer code = dc.read("$.code");
		String title = dc.read("$.body.items[0].title");
		
		assertThat(code).isEqualTo(1);
		assertThat(title).isEqualTo("junit");
	}
	
	@Sql("classpath:db/tableInit.sql")
    @Test
    @DisplayName("책 한권")
    public void getBookOne_test() {
		
        // given
        Integer id = 1;

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.GET, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit");
    }
	
	@Sql("classpath:db/tableInit.sql")
    @Test
    @DisplayName("책 삭제")
    public void deleteBook_test() {
		
        // given
        Integer id = 1;

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.DELETE, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");

        assertThat(code).isEqualTo(1);
    }
	
	@Sql("classpath:db/tableInit.sql")
    @Test
    @DisplayName("책 수정")
    public void updateBook_test() throws JsonProcessingException {
		
        // given
        Integer id = 1;
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("spring");
        bookSaveReqDto.setAuthor("김태희");
        
        String body = om.writeValueAsString(bookSaveReqDto);

        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.PUT, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("spring");
        assertThat(author).isEqualTo("김태희");
    }

}
