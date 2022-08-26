package com.cos.junit.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BookRepositoryTest {
	
	 @Autowired
	 private BookRepository bookRepository;
	 
	 // 책 등록
     @Test
     public void 책등록_test() {
    	 System.out.print("책등록 test준비");
     }

}
