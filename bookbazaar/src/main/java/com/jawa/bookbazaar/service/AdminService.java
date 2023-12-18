package com.jawa.bookbazaar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jawa.bookbazaar.exception.InvalidBookPriceException;
import com.jawa.bookbazaar.exception.InvalidOrderIdException;
import com.jawa.bookbazaar.exception.InvalidOrderStatusException;
import com.jawa.bookbazaar.exception.InvalidUserIdException;
import com.jawa.bookbazaar.exception.OrderNotFoundException;
import com.jawa.bookbazaar.model.Book;
import com.jawa.bookbazaar.model.BookItem;
import com.jawa.bookbazaar.model.Order;
import com.jawa.bookbazaar.model.User;
import com.jawa.bookbazaar.model.enums.OrderStatus;
import com.jawa.bookbazaar.model.enums.UserRole;
import com.jawa.bookbazaar.repository.BookRepository;
import com.jawa.bookbazaar.repository.OrderRepository;
import com.jawa.bookbazaar.repository.UserRepository;

@Service
public class AdminService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private BookService bookService;

	public List<Book> getAllBooks(int pageNo, int count) {
		
		if(pageNo == -1) {
			return bookRepository.findAll();
		}
		Sort sort = Sort.by("id").ascending();
		Pageable pageable = PageRequest.of(pageNo, count, sort);
		Page<Book> page = bookRepository.findAll(pageable);
		return page.getContent();
	}
	
	public long getBooksCount() {
		return bookRepository.count();
	}

	public List<User> getAllUsers(int pageNo, int count) {
		
		if(pageNo == -1) {
			return userRepository.findAll();
		}
		
		Sort sort = Sort.by("id").ascending();
		Pageable pageable = PageRequest.of(pageNo, count, sort);
		Page<User> page = userRepository.findAll(pageable);
		return page.getContent();
	}
	
	public long getUsersCount() {
		return userRepository.count();
	}

	public List<Order> getAllOrders(int pageNo, int count) {
		
		if(pageNo == -1) {
			return orderRepository.findAll();
		}
		
		Sort sort = Sort.by("orderedDate").ascending();
		Pageable pageable = PageRequest.of(pageNo, count, sort);
		Page<Order> page = orderRepository.findAll(pageable);
		return page.getContent();
	}
	
	public long getOrdersCount() {
		return orderRepository.count();
	}
	
	public List<Order> getPendingOrders(int pageNo, int count) {
		
		if(pageNo == -1) {
			return orderRepository.findByOrderStatusNot(OrderStatus.DELIVERED);
		}
		Sort sort = Sort.by("orderedDate").ascending();
		Pageable pageable = PageRequest.of(pageNo, count, sort);
		Page<Order> page = orderRepository.findByOrderStatusNot(OrderStatus.DELIVERED, pageable);
		return page.getContent();
	}
	
	public List<BookItem> getOrderItems(int orderid) {
		Optional<Order> orderOptional = orderRepository.findById(orderid);
		if(orderOptional.isEmpty()) {
			throw new InvalidOrderIdException("There is no order available for this ID");
		}
		Order order = orderOptional.get();
		return order.getOrderItems();
	}

	public User getAdminDetails() {
		User admin = userRepository.findByRole(UserRole.ADMIN).get();
		return admin;
	}

	public void updateBookInfo(long isbn, float price, int quantity) {
		Book book = bookService.getBookByIsbn(isbn);
		if(price == 0.0) {
			throw new InvalidBookPriceException("Book Price cannot be empty when updating details");
		}
		book.setPrice(price);
		book.setStockQuantity(quantity);
		bookRepository.save(book);
	}

	public void updateOrderStatus(int orderid, int status) {
		Optional<Order> orderOptional = orderRepository.findById(orderid);
		if (orderOptional.isEmpty()) {
			throw new OrderNotFoundException(String.format("There is no order found for order id %d", orderid));
		}

		Order order = orderOptional.get();
		switch (status) {
		case 1:
			order.setOrderStatus(OrderStatus.ORDERED);
			break;
		case 2:
			order.setOrderStatus(OrderStatus.SHIPPED);
			break;
		case 3:
			order.setOrderStatus(OrderStatus.DELIVERED);
			break;
		default:
			throw new InvalidOrderStatusException(String.format("Order Status %d is invalid", status));
		}

		orderRepository.save(order);
	}

	public Book getBookByIsbn(long isbn) {
		return bookService.getBookByIsbn(isbn);
	}

	public User getUserById(int userid) {
		Optional<User> userOptional = userRepository.findById(userid);
		if (userOptional.isEmpty()) {
			throw new InvalidUserIdException(String.format("No user found for user id %d", userid));
		}

		return userOptional.get();
	}

	public Order getOrderById(int orderid) {
		Optional<Order> orderOptional = orderRepository.findById(orderid);
		if (orderOptional.isEmpty()) {
			throw new InvalidOrderIdException(String.format("No Order found for user id %d", orderid));
		}

		return orderOptional.get();
	}

}
