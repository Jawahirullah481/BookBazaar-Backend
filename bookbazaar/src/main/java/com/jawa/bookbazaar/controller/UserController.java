package com.jawa.bookbazaar.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.jawa.bookbazaar.exception.AddressNotAvailableException;
import com.jawa.bookbazaar.model.Book;
import com.jawa.bookbazaar.model.BookItem;
import com.jawa.bookbazaar.model.Cart;
import com.jawa.bookbazaar.model.Order;
import com.jawa.bookbazaar.model.User;
import com.jawa.bookbazaar.model.UserAddress;
import com.jawa.bookbazaar.model.UserBookInfo;
import com.jawa.bookbazaar.service.BookService;
import com.jawa.bookbazaar.service.DataService;
import com.jawa.bookbazaar.service.UserService;


@RestController
@RequestMapping(path = {"/", "/users/{userid}"})
public class UserController {
	
	@Autowired
	private DataService dataService;
	@Autowired
	private UserService userService;

	
	@GetMapping("/")
	public void addData() {
		dataService.addData();
	}
	
	
	@GetMapping("/books/{isbn}")
	public MappingJacksonValue getUserBookInfo(@PathVariable int userid, @PathVariable long isbn) {
		
		UserBookInfo userBookInfo = userService.getUserBookInformation(userid, isbn);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userBookInfo);
		SimpleFilterProvider filters = new SimpleFilterProvider()
				.addFilter("UserBookInfoFilter", SimpleBeanPropertyFilter.serializeAll())
				.addFilter("BookFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));
		mappingJacksonValue.setFilters(filters);
						
		return mappingJacksonValue;
	}
	
	
	@GetMapping("/favourites")
	public MappingJacksonValue getUserFavourites(@PathVariable int userid) {
		
		Set<Book> favouriteBooks = userService.getFavourites(userid);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(new ArrayList<Book>(favouriteBooks));
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("id");
		FilterProvider filters = new SimpleFilterProvider().addFilter("BookFilter", filter);
		mappingJacksonValue.setFilters(filters);
		
		return mappingJacksonValue;
	}
	
	
	@PutMapping("/favourites/{isbn}")
	public ResponseEntity<Object> addToUserFavourites(@PathVariable int userid, @PathVariable long isbn) {
		
		if(userService.addBookToFavourite(isbn, userid))
			return new ResponseEntity<Object>(HttpStatus.OK);
		
		return new ResponseEntity<Object>(String.format("Book of isbn %d already present in the favourites of user id %d", isbn, userid), HttpStatus.FORBIDDEN);
	}
	
	@DeleteMapping("/favourites/{isbn}")
	public ResponseEntity<Object> deleteFromUserFavourites(@PathVariable int userid, @PathVariable long isbn) {
		
		if(userService.deleteBookFromFavourites(isbn, userid))
			return new ResponseEntity<Object>(HttpStatus.OK);
		
		return new ResponseEntity<Object>(String.format("Book of isbn %d not present in the favourites of user id %d", isbn, userid), HttpStatus.FORBIDDEN);
	}
	
	
	@GetMapping("/cart")
	public MappingJacksonValue getUserCart(@PathVariable int userid) {
		Cart cart = userService.getUserCart(userid);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(cart);
		SimpleBeanPropertyFilter cartFilter = SimpleBeanPropertyFilter.serializeAllExcept("user", "id");
		SimpleBeanPropertyFilter bookFilter = SimpleBeanPropertyFilter.serializeAllExcept("id");		
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("CartFilter", cartFilter).addFilter("BookFilter", bookFilter);
		mappingJacksonValue.setFilters(filterProvider);
		
		return mappingJacksonValue;
	}
	
	
	@PutMapping("/cart/{isbn}")
	public ResponseEntity<Object> addToUserCart(@PathVariable int userid, @PathVariable long isbn) {
		
		if(userService.addBookToCart(isbn, userid))
			return new ResponseEntity<Object>(HttpStatus.OK);
		
		return new ResponseEntity<Object>(String.format("Book of isbn %d already present in the cart of user id %d", isbn, userid), HttpStatus.BAD_REQUEST);
	}
	
	
	@DeleteMapping("/cart/{isbn}")
	public ResponseEntity<Object> deleteFromUserCart(@PathVariable int userid, @PathVariable long isbn) {
				
		if(userService.deleteBookFromCart(isbn, userid))
			return new ResponseEntity<Object>(HttpStatus.OK);
		
		return new ResponseEntity<Object>(String.format("Book of isbn %d not present in the cart of user id %d", isbn, userid), HttpStatus.FORBIDDEN);
	}
	
	
	@PutMapping("/cart/{isbn}/{quantity}")
	public ResponseEntity<Object> updateCartItemCount(@PathVariable int userid, @PathVariable long isbn, @PathVariable int quantity) {
		if(quantity < 0)
			return new ResponseEntity<Object>(String.format("Cart Item Quantity shouldn't be less than 1", isbn, userid), HttpStatus.BAD_REQUEST);
		
		if(userService.updateCartItemCount(isbn, userid, quantity))
			return new ResponseEntity<Object>(HttpStatus.OK);

		return new ResponseEntity<Object>(String.format("Book of isbn %d not present in the cart of user id %d", isbn, userid), HttpStatus.FORBIDDEN);
	}
	
	
	@GetMapping("/orders")
	public MappingJacksonValue getOrdersFromUser(@PathVariable int userid) {
		
		Set<Order> orders = userService.getOrders(userid);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(new ArrayList<Order>(orders));
		SimpleBeanPropertyFilter bookFilter = SimpleBeanPropertyFilter.serializeAllExcept("id");
		SimpleBeanPropertyFilter orderFilter = SimpleBeanPropertyFilter.serializeAllExcept("user");
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("OrderFilter", orderFilter).addFilter("BookFilter", bookFilter);
		mappingJacksonValue.setFilters(filterProvider);
		
		return mappingJacksonValue;
	}
	
	@PostMapping(path = "/buy/{isbn}")
	public ResponseEntity<Object> buyBook(@PathVariable int userid, @PathVariable long isbn) {
		
		if(userService.getUserById(userid).getAddress() == null) {
			throw new AddressNotAvailableException("Order cannot be placed without Address");
		}
		
		userService.addOrder(userid, isbn);
		return new ResponseEntity<Object>("order submitted", HttpStatus.ACCEPTED);
	}
	
	
	// This is not currenty used
	@PostMapping(path = "/buy")
	public ResponseEntity<Object> addOrder(@PathVariable int userid, @RequestBody List<Map<String, Object>> orderItems) {
		
		if(userService.getUserById(userid).getAddress() == null) {
			throw new AddressNotAvailableException("Order cannot be placed without Address");
		}
		
		userService.addOrders(userid, orderItems);
		return new ResponseEntity<Object>("order submitted", HttpStatus.ACCEPTED);
	}
	
	
	// This is used to get buyable items from cart. Because, cart may contain cart items which quantity is more than available stock.
	@GetMapping(path = "/buy/cart")
	public MappingJacksonValue getBuyItemsFromCart(@PathVariable int userid) {
		Cart cart = userService.getBuyCart(userid);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(cart);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("CartFilter", SimpleBeanPropertyFilter.filterOutAllExcept("cartItems", "totalCost"))
				.addFilter("BookFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));
		mappingJacksonValue.setFilters(filterProvider);
		return mappingJacksonValue;
	}
	
	@PostMapping(path = "/buy/cart")
	public ResponseEntity<Object> buyFromCart(@PathVariable int userid) {
		
		if(userService.getUserById(userid).getAddress() == null) {
			throw new AddressNotAvailableException("Order cannot be placed without Address");
		}
		userService.addOrderFromCart(userid);
		return new ResponseEntity<Object>("order submitted", HttpStatus.ACCEPTED);
	}
	
	
	@GetMapping("/account")
	public MappingJacksonValue getUserAccountDetails(@PathVariable int userid) {
		User user = userService.getUserById(userid);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(user);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id", "username", "email"));
		mappingJacksonValue.setFilters(filterProvider);
		
		return mappingJacksonValue;
	}
	
	
	@GetMapping("/address")
	public MappingJacksonValue userAddress(@PathVariable int userid) {
		UserAddress userAddress = userService.getUserById(userid).getAddress();
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userAddress);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("AddressFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));
		mappingJacksonValue.setFilters(filterProvider);
		
		return mappingJacksonValue;
	}
	
	@GetMapping("/userdetails")
	public MappingJacksonValue getUserDetails(@PathVariable int userid) {
		User user = userService.getUserDetails(userid);
		System.out.println("User Details : " + user);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(user);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id", "username", "email", "address")).addFilter("AddressFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));
		mappingJacksonValue.setFilters(filterProvider);
		
		return mappingJacksonValue;
	}
	
	@PostMapping("/address")
	public ResponseEntity<Object> updateAddress(@PathVariable int userid, @RequestBody UserAddress userAddress) {
		System.out.println("BOOK BAZAAR : " + "UserAddress.pin = " + userAddress.getPin() + "   UserAddress.mobile = " + userAddress.getMobile());
		userService.updateUserAddress(userid, userAddress);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
}