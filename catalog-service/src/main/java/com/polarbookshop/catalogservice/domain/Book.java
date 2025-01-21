package com.polarbookshop.catalogservice.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record Book ( // 불가변 객체인 레코드로 구현.
	
	@Id
	Long id,
	
	@NotBlank(message = "The book ISBN must be defined.")
	@Pattern(
			regexp = "^([0-9]{10}|[0-9]{13})$",
			message = "The ISBN format must be valid."
	)
	String isbn, // 책을 고유하게 식별.
	
	@NotBlank(
			message = "The book title must be defined."
	)
	String title,
	
	@NotBlank(message = "The book author must be defined.")
	String author,
	
	@NotNull(message = "The book price must be defined.")
	@Positive( // 0보다 큰 값 가지게 함.
			message = "The book price must be greater than zero."
	)
	Double price,
	
	String publisher,
	
	@CreatedDate
	Instant createdDate,
	
	@LastModifiedDate
	Instant lastModifiedDate,
	
	@Version // 낙관적 잠긍을 위해 사용되는 엔티티 버전 번호.
	int version
){
	// 모든 인수를 갖는 생성자를 사요하는 것은 테스트 데이터 생성과 같은 상황에서 코드 작성 번거로움.
	// 편의성을 위해 비즈니스 로직과 관련된 필드만 전달해, 객체를 생성하도록 정적 팩토리 메서드를 추가.
	public static Book of(
		String isbn, String title, String author, Double price, String publisher) {
			return new Book(
				// id가 null이고, version이 0이면 새로운 엔티티로 인식.
				null, isbn, title, author, price, publisher, null, null, 0
		);
	}
}
