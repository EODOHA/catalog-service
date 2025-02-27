package com.polarbookshop.catalogservice.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import com.polarbookshop.catalogservice.domain.Book;

@JsonTest // JSON 직렬화에 중점을 둔 테스트 클래스임을 나타냄.
public class BookJsonTests {

	@Autowired
	// JSON 직렬화 및 역직렬화를 확인하기 위한 유틸리티 클래스.
	private JacksonTester<Book> json;
	
	@Test
	void testSerialize() throws Exception {
		var book = Book.of("1234567890", "Title", "Author", 9.90, "Polarsophia");
		var jsonContent = json.write(book);
		// JsonPath 형식을 사용, JSON 객체를 탐색하고 자바의 JSON 변환을 확인함.
		assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
			.isEqualTo(book.isbn());
		assertThat(jsonContent).extractingJsonPathStringValue("@.title")
			.isEqualTo(book.title());
		assertThat(jsonContent).extractingJsonPathStringValue("@.author")
			.isEqualTo(book.author());
		assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
			.isEqualTo(book.price());
		assertThat(jsonContent).extractingJsonPathStringValue("@.publisher")
			.isEqualTo(book.publisher());
		// 직렬화, 역직렬화에 @.id, @.version, @.createdDate, @.lastmodifiedDate
		// null, 0인 부분은 제외 가능.
		// 다 해보고 싶으면 임의의 값 직접 입력.
	}
	
	// 커밋 테스트용 주석.
	
	@Test
	void testDeserialize() throws Exception {
		// 자바 텍스트 블록 기능을 사용해, JSON 객체를 정의함.
		var content = """
			{
					"isbn": "1234567890",
					"title": "Title",
					"author": "Author",
					"price": 9.90,
					"publisher": "Polarsophia"
			}
			""";
		// JSON에서 자바 객체로의 변환을 확인함.
		assertThat(json.parse(content))
		.usingRecursiveComparison()
		.isEqualTo(Book.of("1234567890", "Title", "Author", 9.90, "Polarsophia"));
	}
}
