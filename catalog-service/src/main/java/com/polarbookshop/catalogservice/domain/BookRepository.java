package com.polarbookshop.catalogservice.domain;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BookRepository extends CrudRepository<Book, Long>{
//	Iterable<Book> findAll(); // CrudRepository에서 기본 제공
	Optional<Book> findByIsbn(String isbn);
	boolean existsByIsbn(String isbn);
	
	// Book save(Book book); // CrudRepository에서 기본 제공
	
	// 사용자 정의 쿼리 메서드는 자동 트랜잭션이 불가능!!
	@Modifying
	@Transactional // 메서드가 트랜잭션으로 실행됨을 나타냄.
	@Query("delete from Book where isbn = :isbn")
	void deleteByIsbn(String isbn);
}
