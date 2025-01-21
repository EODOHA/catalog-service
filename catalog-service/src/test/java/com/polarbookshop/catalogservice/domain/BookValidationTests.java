package com.polarbookshop.catalogservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

// 단위 테스트
class BookValidationTests {
	private static Validator validator;
	
	@BeforeAll // 클래스 내 테스트 실행 전, 가장 먼저 실행할 코드 블록임을 나타냄.
	static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@Test // 테스트 케이스임을 나타냄.
	void whenAllFieldsCorrectThenValidationSucceeds() {
		// 유효한 ISBN으로 책을 생성함.
		var book = Book.of("1234567890", "Title", "Author", 9.90, "Polarsophia");
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).isEmpty(); // 유효성 검사에서 오류가 없음을 확인함.
	}
	
	@Test
	void whenIsbnDefinedButIncorrectThenValidationFails() {
		// 유효하지 않은 ISBN으로 책을 생성함.
		var book = Book.of("a234567890", "Title", "Author", 9.90, "Polarsophia");
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator().next().getMessage())
			// 유효성 검사 제약 조건 위반이 잘못된 ISBN에 대한 것인지 확인함. 
			.isEqualTo("The ISBN format must be valid.");
	}
}
