package com.polarbookshop.catalogservice.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.BookService;

// 스프링 MVC 컴포넌트에 중점을 두고,
// 명시적으로는 BookController 클래스를 타깃으로하는 테스트 클래스임을 나타냄.
@WebMvcTest(BookController.class)
public class BookControllerMvcTests {

	@Autowired
	private MockMvc mockMvc; // 모의 환경에서 웹 계층을 테스트하기 위한 유틸리티 클래스.
	
	@SuppressWarnings("removal")
	@MockBean // 스프링 애플리케이션 콘텍스트에 BookService의 모의 객체를 추가함.
	private BookService bookService;
	
	@Test
	void whenGetBookNotExistingThenShouldReturn404() throws Exception {
		String isbn = "73737313940";
		given(bookService.viewBookDetails(isbn))
			// 모의 빈이 어떻게 작동할 것인지 규정함.
			.willThrow(BookNotFoundException.class);
		mockMvc
			// MockMVC는 HTTP GET 요청을 수행하고 결과를 확인하기 위해 사용함.
			.perform(get("/books/" + isbn))
			// 응답이 "404 발견되지 않음" 상태를 가질 것으로 예상함.
			.andExpect(status().isNotFound());
	}
}
