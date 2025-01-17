package com.polarbookshop.catalogservice.demo;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;

@Component
@Profile("testdata") // 이 클래스는 testdata 프로파일에 할당.
					 // testdata 프로파일이 활성화될 때만 로드됨.
public class BookDataLoader {
	private final BookRepository bookRepository;
	
	public BookDataLoader(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void loadBookTestData() {
		var book1 = new Book("1234567891", "Northern Lights", "Lyra Silverstar", 9.90);
		var book2 = new Book("1234567892", "Polar Journey", "Iorek Polarson", 12.90);
		bookRepository.save(book1);
		bookRepository.save(book2);
	}
}
