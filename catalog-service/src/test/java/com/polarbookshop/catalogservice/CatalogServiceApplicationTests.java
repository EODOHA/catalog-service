package com.polarbookshop.catalogservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.polarbookshop.catalogservice.domain.Book;

// 통합 테스트
@SpringBootTest(
		// 완전한 스프링 웹 애플리케이션 콘텍스트와
		// 임의의 포트를 듣는 서블릿 컨테이너를 로드한다.
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("integration") // application-integration.yml에서 설정을 로드하기 위해 "integration" 프로파일을 활성화.
class CatalogServiceApplicationTests {
	
	@Autowired
	// 테스트를 위해 REST 엔드포인트를 호출할 유틸리티.
	private WebTestClient webTestClient;

	@Test
	void whenPostRequestThenBookCreated() {
		var expectedBook = Book.of("1231231231", "Title", "Author", 9.90, "Polarsophia");
		
		webTestClient
			.post() // HTTP POST 요청 보냄.
			.uri("/books") // "books" 엔드포인트로 요청 보냄.
			.bodyValue(expectedBook) // 요청 본문에 Book 객체 추가함.
			.exchange() // 요청을 전송함.
			.expectStatus().isCreated() // HTTP 응답이 "201 생성" 상태 갖는지 확인함.
			.expectBody(Book.class).value(actualBook -> {
				assertThat(actualBook).isNotNull(); // HTTP 응답의 본문이 널값이 아닌지 확인함.
				assertThat(actualBook.isbn())
					.isEqualTo(expectedBook.isbn()); // 생성된 객체가 예상과 동일한지 확인함.
			});
			
	}

}
