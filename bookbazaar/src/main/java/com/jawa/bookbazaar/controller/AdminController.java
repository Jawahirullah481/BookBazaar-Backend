package com.jawa.bookbazaar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.jawa.bookbazaar.model.Book;
import com.jawa.bookbazaar.model.BookItem;
import com.jawa.bookbazaar.model.Order;
import com.jawa.bookbazaar.model.User;
import com.jawa.bookbazaar.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	
	@GetMapping("/books")
	public MappingJacksonValue getAllAvailableBooks(@RequestParam("page") int pageNo,
			@RequestParam("count") int count) {
		List<Book> books = adminService.getAllBooks(pageNo, count);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(books);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("BookFilter",
				SimpleBeanPropertyFilter.serializeAll());
		mappingJacksonValue.setFilters(filterProvider);

		return mappingJacksonValue;
	}
	
	@GetMapping("/books/count")
	public long getTotalBooksCount() {
		return adminService.getBooksCount();
	}
	
	
	@GetMapping("/books/search")
	public MappingJacksonValue searchBook(@RequestParam long isbn) {
		Book book = adminService.getBookByIsbn(isbn);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(book);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("BookFilter",
				SimpleBeanPropertyFilter.serializeAll());
		mappingJacksonValue.setFilters(filterProvider);

		return mappingJacksonValue;
	}
	
	
	@PutMapping("/books/{isbn}/{price}/{quantity}")
	public ResponseEntity<Object> updateBookQuantity(@PathVariable long isbn, @PathVariable float price, @PathVariable int quantity) {
		adminService.updateBookInfo(isbn, price, quantity);
		return new ResponseEntity<Object>(HttpStatus.OK); 
	}

	@GetMapping("/users")
	public MappingJacksonValue getAllUsers(@RequestParam("page") int pageNo, @RequestParam("count") int count) {
		List<User> users = adminService.getAllUsers(pageNo, count);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(users);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider()
				.addFilter("UserFilter", SimpleBeanPropertyFilter.serializeAllExcept("password", "role", "orders", "cart", "favourites"))
				.addFilter("AddressFilter", SimpleBeanPropertyFilter.filterOutAllExcept("address", "mobile"));
		mappingJacksonValue.setFilters(filterProvider);

		return mappingJacksonValue;
	}
	
	@GetMapping("/users/count")
	public long getTotalUsersCount() {
		return adminService.getUsersCount();
	}
	
	
	@GetMapping("/users/search")
	public MappingJacksonValue searchUser(@RequestParam int userid) {
		User user = adminService.getUserById(userid);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(user);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider()
				.addFilter("UserFilter", SimpleBeanPropertyFilter.serializeAllExcept("password", "role", "orders", "cart", "favourites"))
				.addFilter("AddressFilter", SimpleBeanPropertyFilter.filterOutAllExcept("address", "mobile"));
		mappingJacksonValue.setFilters(filterProvider);

		return mappingJacksonValue;
	}
	
	@GetMapping("/userdetails/{userid}")
	public MappingJacksonValue getUserDetails(@PathVariable int userid) {
		User user = adminService.getUserById(userid);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(user);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider()
				.addFilter("UserFilter", SimpleBeanPropertyFilter.filterOutAllExcept("username", "email", "address"))
				.addFilter("AddressFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));
		mappingJacksonValue.setFilters(filterProvider);

		return mappingJacksonValue;
	}
	
	
	@GetMapping("/orders")
	public MappingJacksonValue getAllOrders(@RequestParam("page") int pageNo, @RequestParam("count") int count) {
		
		List<Order> orders = adminService.getAllOrders(pageNo, count);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(orders);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider()
				.addFilter("OrderFilter", SimpleBeanPropertyFilter.serializeAllExcept("orderItems"))
				.addFilter("BookFilter", SimpleBeanPropertyFilter.serializeAll())
				.addFilter("UserFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id"));
		mappingJacksonValue.setFilters(filterProvider);
		
		return mappingJacksonValue;
		
	}
	
	@GetMapping("/orders/count")
	public long getTotalOrdersCount() {
		return adminService.getOrdersCount();
	}
	

	
	@GetMapping("/orders/pending")
	public MappingJacksonValue getPendingOrders(@RequestParam("page") int pageNo, @RequestParam("count") int count) {
		
		List<Order> orders = adminService.getPendingOrders(pageNo, count);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(orders);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider()
				.addFilter("OrderFilter", SimpleBeanPropertyFilter.serializeAllExcept("orderItems"))
				.addFilter("BookFilter", SimpleBeanPropertyFilter.serializeAll())
				.addFilter("UserFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id"));
		mappingJacksonValue.setFilters(filterProvider);
		
		return mappingJacksonValue;
		
	}
	

	
	@GetMapping("/orders/search")
	public MappingJacksonValue searchOrder(@RequestParam int orderid) {
		Order order = adminService.getOrderById(orderid);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(order);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider()
				.addFilter("OrderFilter", SimpleBeanPropertyFilter.serializeAll())
				.addFilter("BookFilter", SimpleBeanPropertyFilter.serializeAll())
				.addFilter("UserFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id"));
		mappingJacksonValue.setFilters(filterProvider);
		
		return mappingJacksonValue;
	}
	
	@GetMapping("/orders/{orderid}/items")
	public MappingJacksonValue getOrderItems(@PathVariable int orderid) {
		List<BookItem> orderItems = adminService.getOrderItems(orderid);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(orderItems);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("BookFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));
		mappingJacksonValue.setFilters(filterProvider);
		return mappingJacksonValue;
	}
	
	
	@PutMapping("/orders/{orderid}/status/{status}")
	public ResponseEntity<Object> setOrderStatus(@PathVariable int orderid, @PathVariable int status) {
		adminService.updateOrderStatus(orderid, status);
		return new ResponseEntity<Object>(HttpStatus.OK); 
	}
	
	
	@GetMapping("/account")
	public MappingJacksonValue getAdminDetails() {
		User user = adminService.getAdminDetails();
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(user);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserFilter", SimpleBeanPropertyFilter.filterOutAllExcept("username", "email", "password", "role"));
		mappingJacksonValue.setFilters(filterProvider);
		
		return mappingJacksonValue;
	}
	
	
}
