package com.jawa.bookbazaar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jawa.bookbazaar.googleapi.service.GoogleBookService;
import com.jawa.bookbazaar.model.Book;
import com.jawa.bookbazaar.repository.BookRepository;

@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private GoogleBookService googleBookService;

	public int getBookStock(long isbn) {
		Optional<Book> book = bookRepository.findByIsbn(isbn);
		
		if(book.isPresent()) {
			return book.get().getStockQuantity();
		}
		
		return 0;
	}

	public Book getBookByIsbn(long isbn) {
		
		Optional<Book> bookOptional = bookRepository.findByIsbn(isbn);
		
		Book book;
		
		if(bookOptional.isPresent()) {
			book = bookOptional.get();			
		}else {
			book = googleBookService.getBookByIsbn(isbn);
			bookRepository.save(book);
		}
		
		return book;	
	}

	
	public List<Book> getAllAvailableBooks() {
		return bookRepository.findAll();
	}
	
}
