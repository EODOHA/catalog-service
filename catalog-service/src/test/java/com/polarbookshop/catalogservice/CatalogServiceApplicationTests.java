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
    void whenGetRequestWithIdThenBookReturned() {
        var bookIsbn = "1231231230";
        var bookToCreate = Book.of(bookIsbn, "Title", "Author", 9.90, "Polarsophia");
        Book expectedBook = webTestClient
                .post()
                .uri("/books")
                .bodyValue(bookToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class).value(book -> assertThat(book).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .get()
                .uri("/books/" + bookIsbn)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Book.class).value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
                });
    }

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
	
	@Test
    void whenPutRequestThenBookUpdated() {
        var bookIsbn = "1231231232";
        var bookToCreate = Book.of(bookIsbn, "Title", "Author", 9.90, "Polarsophia");
        Book createdBook = webTestClient
                .post()
                .uri("/books")
                .bodyValue(bookToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class).value(book -> assertThat(book).isNotNull())
                .returnResult().getResponseBody();
        var bookToUpdate = Book.of(createdBook.isbn(), createdBook.title(), createdBook.author(), 7.95, createdBook.publisher());

        webTestClient
                .put()
                .uri("/books/" + bookIsbn)
                .bodyValue(bookToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class).value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.price()).isEqualTo(bookToUpdate.price());
                });
    }
	
	@Test
    void whenDeleteRequestThenBookDeleted() {
        var bookIsbn = "1231231233";
        var bookToCreate = Book.of(bookIsbn, "Title", "Author", 9.90, "Polarsophia");
        webTestClient
                .post()
                .uri("/books")
                .bodyValue(bookToCreate)
                .exchange()
                .expectStatus().isCreated();

        webTestClient
                .delete()
                .uri("/books/" + bookIsbn)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get()
                .uri("/books/" + bookIsbn)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).value(errorMessage ->
                        assertThat(errorMessage).isEqualTo("The book with ISBN " + bookIsbn + " was not found.")
                );
    }
}
