package com.polarbookshop.catalogservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.polarbookshop.catalogservice.config.DataConfig;

@DataJdbcTest // 스프링 데이터 JDBC 컴포넌트를 집중적으로 테스트하는 클래스임을 나타냄.
@Import(DataConfig.class) // 데이터 설정을 임포트한다(감사 활성화 위해 필요)
@AutoConfigureTestDatabase( // 테스트컨테이너를 이용해야 하기 때문에 내장 테스트 DB를 비활성화.
		replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("integration") // application-integration.yml에서 설정 로드를 위해, "integration" 프로파일을 활성화.
class BookRepositoryJdbcTests {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private JdbcAggregateTemplate jdbcAggregateTemplate; // DB와 상호작용 위한 하위 수준의 객체.
	
	@Test
	void findBookByIsbnWhenExisting() {
		var bookIsbn = "1234561237";
		var book = Book.of(bookIsbn, "Title", "Author", 12.90);
		// 아래 참조변수는 테스트에 필요한 데이터를 준비하는 데 사용.
		jdbcAggregateTemplate.insert(book);
		Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);
		
		assertThat(actualBook).isPresent();
		assertThat(actualBook.get().isbn())
			.isEqualTo(book.isbn());
	}
}
