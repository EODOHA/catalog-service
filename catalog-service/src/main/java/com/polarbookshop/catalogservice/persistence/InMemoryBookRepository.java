package com.polarbookshop.catalogservice.persistence;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;

@Repository // 클래스가 스프링에 의해 관리되는 리포지터리임을 표시하는 애너테이션.
public class InMemoryBookRepository implements BookRepository {
	private static final Map<String, Book> books = new ConcurrentHashMap<>();
	
	@Override
	public Iterable<Book> findAll() {
		return books.values();
	}
	
	@Override
	public Optional<Book> findByIsbn(String isbn) {
		return existsByIsbn(isbn) ? Optional.of(books.get(isbn)) : Optional.empty();
	}
	
	@Override
	public boolean existsByIsbn(String isbn) {
		return books.get(isbn) != null;
	}
	
	@Override
	public Book save(Book book) {
		books.put(book.isbn(), book);
		return book;
	}
	
	@Override
	public void deleteByIsbn(String isbn) {
		books.remove(isbn);
	}
}
