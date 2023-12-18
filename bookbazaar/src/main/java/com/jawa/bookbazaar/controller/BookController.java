package com.jawa.bookbazaar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.jawa.bookbazaar.exception.InvalidBookNameException;
import com.jawa.bookbazaar.googleapi.service.GoogleBookService;
import com.jawa.bookbazaar.model.Book;
import com.jawa.bookbazaar.model.UserBookInfo;
import com.jawa.bookbazaar.service.BookService;
import com.jawa.bookbazaar.service.UserService;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private GoogleBookService googleBookService;
	@Autowired
	private UserService userService;
	@Autowired
	private BookService bookService;
	
	@GetMapping
	public MappingJacksonValue getAllBooks() {
		List<Book> books = bookService.getAllAvailableBooks();
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(books);
		SimpleFilterProvider filter = new SimpleFilterProvider().addFilter("BookFilter",
				SimpleBeanPropertyFilter.serializeAllExcept("id"));
		mappingJacksonValue.setFilters(filter);
		return mappingJacksonValue;
	}
	
	@GetMapping("/info/{isbn}")
	public MappingJacksonValue getBookInfo(@PathVariable long isbn) {

		Book book  = bookService.getBookByIsbn(isbn);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(book);
		SimpleFilterProvider filters = new SimpleFilterProvider()
				.addFilter("BookFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));
		mappingJacksonValue.setFilters(filters);
		return mappingJacksonValue;
		
	}
	
	@GetMapping("/popularBooks")
	public MappingJacksonValue getPopularBooks() {
		List<Book> books = bookService.getAllAvailableBooks();
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(books);
		SimpleFilterProvider filter = new SimpleFilterProvider().addFilter("BookFilter",
				SimpleBeanPropertyFilter.serializeAllExcept("id"));
		mappingJacksonValue.setFilters(filter);
		return mappingJacksonValue;
	}
	

	@GetMapping("/search")
	public MappingJacksonValue searchBookByName(@RequestParam("q") String searchText) throws InvalidBookNameException {
		List<Book> books = googleBookService.searchByName(searchText);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(books);
		SimpleFilterProvider filter = new SimpleFilterProvider().addFilter("BookFilter",
				SimpleBeanPropertyFilter.serializeAllExcept("id"));
		mappingJacksonValue.setFilters(filter);
		return mappingJacksonValue;
	}
	

	@GetMapping("/view/{isbn}")
	public MappingJacksonValue getViewBookInfo(@PathVariable long isbn) {

		UserBookInfo userBookInfo = userService.getUserBookInformation(0, isbn);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userBookInfo);
		SimpleFilterProvider filters = new SimpleFilterProvider()
				.addFilter("UserBookInfoFilter", SimpleBeanPropertyFilter.serializeAll())
				.addFilter("BookFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));
		mappingJacksonValue.setFilters(filters);
		return mappingJacksonValue;
	}

	@GetMapping("/filter")
	public MappingJacksonValue filter(@RequestParam(name = "q") String searchText,
			@RequestParam(name = "author", required = false, defaultValue = "") String author,
			@RequestParam(name = "category", required = false, defaultValue = "") String subject)
			throws InvalidBookNameException {
		List<Book> books = googleBookService.filter(searchText, author, subject);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(books);
		SimpleFilterProvider filter = new SimpleFilterProvider().addFilter("BookFilter",
				SimpleBeanPropertyFilter.serializeAllExcept("id"));
		mappingJacksonValue.setFilters(filter);
		return mappingJacksonValue;
	}

}