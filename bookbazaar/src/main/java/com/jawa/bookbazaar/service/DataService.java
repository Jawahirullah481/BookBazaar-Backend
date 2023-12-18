package com.jawa.bookbazaar.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jawa.bookbazaar.model.Book;
import com.jawa.bookbazaar.model.BookItem;
import com.jawa.bookbazaar.model.Order;
import com.jawa.bookbazaar.model.User;
import com.jawa.bookbazaar.model.UserAddress;
import com.jawa.bookbazaar.model.enums.OrderStatus;
import com.jawa.bookbazaar.repository.BookRepository;
import com.jawa.bookbazaar.repository.UserRepository;

@Service
public class DataService {

	private BookRepository bookRepository;
	private UserRepository userRepository;
	@Autowired
	private BookService bookService;

	@Autowired
	public DataService(BookRepository bookRepository, UserRepository userRepository) {
		this.bookRepository = bookRepository;
		this.userRepository = userRepository;
	}

	public void addData() {

		// -------------------- Books -------------------------

		Book book1 = bookService.getBookByIsbn(9780143454212l);
		book1.setStockQuantity(8);
		book1.setPrice(19.99f);
		

		Book book2 = bookService.getBookByIsbn(9780099549482l);
		book2.setStockQuantity(12);
		book2.setPrice(3.4f);

		Book book3 = bookService.getBookByIsbn(9788129116116l);
		book3.setStockQuantity(5);
		book3.setPrice(25.99f);

		Book book4 = bookService.getBookByIsbn(9781603810135l);
		book4.setStockQuantity(10);
		book4.setPrice(18.75f);

		bookRepository.save(book1);
		bookRepository.save(book2);
		bookRepository.save(book3);
		bookRepository.save(book4);

		// -------------------- User Address ----------------------------

		UserAddress address1 = new UserAddress();
		address1.setAddress("123 Main Street, Anytown");
		address1.setPin(543212);
		address1.setMobile(6234567890L);
		address1.setLandmark("Central Park");
		address1.setCity("Manchester City");
		address1.setCountry("England");
		address1.setState("Manchester");

		UserAddress address2 = new UserAddress();
		address2.setAddress("15B, Oak Avenue, Springfield");
		address2.setPin(123456);
		address2.setMobile(9876543210L);
		address2.setLandmark("City Park");
		address2.setCity("Cxhs Park");
		address2.setCountry("America");
		address2.setState("Sydney");


		// ----------------------- User -------------------------------

		User user1 = new User();
		user1.setUsername("Alice");
		user1.setEmail("alice@example.com");
		user1.setPassword("alice123");
		user1.setAddress(address1);
		user1.addToCart(new BookItem(book4, 2));
		user1.addToFavourite(book3);
		user1.addToFavourite(book4);

		User user2 = new User();
		user2.setUsername("Bob");
		user2.setEmail("bob@gmail.com");
		user2.setPassword("bob2023");
		user2.setAddress(address2);
		user2.addToCart(new BookItem(book1, 2));
		user2.addToCart(new BookItem(book2, 1));
		user2.addToFavourite(book1);
		user2.addToFavourite(book2);
		
		// -------------------Order----------------------------

		Order order1 = new Order();
		order1.addOrderItem(new BookItem(book1, 1));
		order1.setOrderedDate(LocalDate.now());
		order1.setOrderStatus(OrderStatus.DELIVERED);

		Order order2 = new Order();
		order2.addOrderItem(new BookItem(book2, 3));
		order2.addOrderItem(new BookItem(book3, 5));
		order2.setOrderStatus(OrderStatus.ORDERED);
		order2.setOrderedDate(LocalDate.now().minusDays(5L));

		Order order3 = new Order();
		order3.addOrderItem(new BookItem(book1, 5));
		order3.addOrderItem(new BookItem(book4, 1));
		order3.setOrderStatus(OrderStatus.SHIPPED);
		order3.setOrderedDate(LocalDate.now().plusDays(3L));

		user1.addOrder(order1);
		user1.addOrder(order2);
		user2.addOrder(order3);
		

		userRepository.save(user1);
		userRepository.save(user2);
		
		
		//------------------------------------------
		
		UserAddress adminAddress = new UserAddress();
		adminAddress.setAddress("12-A, North Street, London");
		adminAddress.setPin(145623);
		adminAddress.setMobile(7708280124l);
		adminAddress.setCity("New York");
		adminAddress.setCountry("England");
		adminAddress.setState("London");
		adminAddress.setLandmark("Modi Stadium");
		
		User admin = new User();
		admin.setUsername("John");
		admin.setEmail("john234@example.com");
		admin.setPassword("john123");
		admin.setAddress(adminAddress);
		admin.setRoleAsAdmin();
		
		userRepository.save(admin);
		
	}

}
