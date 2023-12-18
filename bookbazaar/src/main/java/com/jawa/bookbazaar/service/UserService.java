package com.jawa.bookbazaar.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jawa.bookbazaar.exception.BookOutOfStockException;
import com.jawa.bookbazaar.exception.DuplicateUserException;
import com.jawa.bookbazaar.model.Book;
import com.jawa.bookbazaar.model.BookItem;
import com.jawa.bookbazaar.model.Cart;
import com.jawa.bookbazaar.model.Order;
import com.jawa.bookbazaar.model.User;
import com.jawa.bookbazaar.model.UserAddress;
import com.jawa.bookbazaar.model.UserBookInfo;
import com.jawa.bookbazaar.repository.BookRepository;
import com.jawa.bookbazaar.repository.CartRepository;
import com.jawa.bookbazaar.repository.UserAddressRepository;
import com.jawa.bookbazaar.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookService bookService;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private CartRepository cartRepository;
	
	public UserBookInfo getUserBookInformation(int userid, long isbn) {

		Book book = bookService.getBookByIsbn(isbn);
		int stock = book.getStockQuantity();

		if (userid == 0) {
			UserBookInfo userBookInfo = new UserBookInfo(book, false, false, stock);
			return userBookInfo;
		}

		User user = userRepository.findById(userid).get();

		Predicate<Book> presentInFavourite = book1 -> {
			if (book1.getIsbn() == isbn) {
				return true;
			}

			return false;
		};

		Predicate<BookItem> presentInCart = bookItem -> {
			if (bookItem.getBook().getIsbn() == isbn) {
				return true;
			}

			return false;
		};

		boolean isAvailableInFavourite = user.getFavourites().stream().anyMatch(presentInFavourite);
		boolean isAvailableInCart = user.getCart().getCartItems().stream().anyMatch(presentInCart);

		UserBookInfo userBookInfo = new UserBookInfo(book, isAvailableInCart, isAvailableInFavourite, stock);

		return userBookInfo;
	}

	public Set<Book> getFavourites(int userid) {
		Set<Book> favouriteBooks = userRepository.findById(userid).get().getFavourites();
		return favouriteBooks;
	}

	public Cart getUserCart(int userid) {
		Cart cart = userRepository.findById(userid).get().getCart();
		return cart;
	}

	public boolean addBookToFavourite(long isbn, int userid) {
		Book book = bookService.getBookByIsbn(isbn);
		User user = userRepository.findById(userid).get();
		boolean isAdded = user.addToFavourite(book);

		if (isAdded) {
			userRepository.save(user);
			return true;
		}

		return false;
	}

	public boolean addBookToCart(long isbn, int userid) {
		Book book = bookService.getBookByIsbn(isbn);
		BookItem bookItem = new BookItem(book);
		User user = userRepository.findById(userid).get();

		if (user.addToCart(bookItem)) {
			userRepository.save(user);
			return true;
		}

		return false;
	}

	public boolean deleteBookFromFavourites(long isbn, int userid) {
		User user = userRepository.findById(userid).get();
		boolean isDeleted = user.deleteFromFavourites(isbn);
		if (isDeleted) {
			userRepository.save(user);
			return true;
		}

		return false;
	}

	public boolean deleteBookFromCart(long isbn, int userid) {
		Book book = bookService.getBookByIsbn(isbn);
		BookItem bookItem = new BookItem(book);
		User user = userRepository.findById(userid).get();
		boolean isDeleted = user.deleteFromCart(bookItem);
		if (isDeleted) {
			userRepository.save(user);
			return true;
		}

		return false;
	}

	public boolean updateCartItemCount(long isbn, int userid, int requestQuantity) {

		User user = userRepository.findById(userid).get();
		Set<BookItem> cartItems = user.getCart().getCartItems();

		Predicate<BookItem> predicate = bookItem1 -> {
			if (bookItem1.getBook().getIsbn() == isbn) {
				return true;
			}
			return false;
		};

		Optional<BookItem> bookItemOptional = cartItems.stream().filter(predicate).findFirst();
		if (bookItemOptional.isEmpty())
			return false;

		BookItem bookItem = bookItemOptional.get();

		bookItem.setQuantity(requestQuantity);
		userRepository.save(user);
		return true;
	}

	public Set<Order> getOrders(int userid) {
		User user = userRepository.findById(userid).get();
		Set<Order> orders = user.getOrders();
		return orders;
	}

	public User getUserById(int userid) {
		return userRepository.findById(userid).get();
	}

	public void updateUserAddress(int userid, UserAddress updatedAddress) {
		User user = userRepository.findById(userid).get();
		UserAddress userAddress = user.getAddress();

		if (userAddress == null) {
			userAddress = new UserAddress();
		}

		System.out.println("Updated User address is : " + updatedAddress);

		userAddress.setAddress(updatedAddress.getAddress());
		userAddress.setCity(updatedAddress.getCity());
		userAddress.setCountry(updatedAddress.getCountry());
		userAddress.setState(updatedAddress.getState());
		userAddress.setPin(updatedAddress.getPin());
		userAddress.setMobile(updatedAddress.getMobile());
		userAddress.setLandmark(updatedAddress.getLandmark());

		user.setAddress(userAddress);
		userRepository.save(user);
	}

	public User updateAccountDetails(int userid, User updatedUser) {

		User user = userRepository.findById(userid).get();

		if (userRepository.existsByUsername(updatedUser.getUsername())) {
			User userWithSameName = userRepository.findByUsername(updatedUser.getUsername()).get();

			if (user.getId() != userWithSameName.getId())
				throw new DuplicateUserException("username already exists");

		}

		user.setUsername(updatedUser.getUsername());
		user.setPassword(updatedUser.getPassword());

		userRepository.save(user);

		return user;
	}

	public void addUser(User user) {
		userRepository.save(user);
	}

	public void addOrders(int userid, List<Map<String, Object>> orderItems) {
		User user = userRepository.findById(userid).get();
		Order order = new Order();

		for (Map<String, Object> orderItem : orderItems) {

			long isbn = (Long) orderItem.get("isbn");
			int orderQuantity = (Integer) orderItem.get("quantity");

			Book book = bookService.getBookByIsbn(isbn);

			if (orderQuantity > book.getStockQuantity()) {
				throw new BookOutOfStockException(String.format(
						"Book of isbn %d not available as you required. Available Quantity : %d, Ordered Quantity : %d",
						isbn, book.getStockQuantity(), orderQuantity));
			}

			BookItem bookItem = new BookItem(book, orderQuantity);
			order.addOrderItem(bookItem);

		}

		user.addOrder(order);
		userRepository.save(user);
	}

	public void addOrder(int userid, long isbn) {

		User user = userRepository.findById(userid).get();
		Book book = bookService.getBookByIsbn(isbn);

		if (book.getStockQuantity() == 0) {
			throw new BookOutOfStockException(String.format("Book of isbn %d is out of stock. Available Quantity : %d",
					isbn, book.getStockQuantity()));
		}

		Order order = new Order();
		BookItem bookItem = new BookItem(book, 1);
		order.addOrderItem(bookItem);
		user.addOrder(order);
		userRepository.save(user);

	}

	public void addOrderFromCart(int userid) {
		User user = userRepository.findById(userid).get();
		Cart cart = user.getCart();
		Order order = new Order();

		for (BookItem cartItem : cart.getCartItems()) {

			Book book = cartItem.getBook();

			int availableQuantity = cartItem.getBook().getStockQuantity();
			int orderQuantity = cartItem.getQuantity();

			if (availableQuantity == 0) {
				continue;
			}

			if (orderQuantity > availableQuantity) {
				orderQuantity = availableQuantity;
			}

			BookItem orderItem = new BookItem(cartItem.getBook(), orderQuantity);
			order.addOrderItem(orderItem);
			book.setStockQuantity(availableQuantity - orderQuantity);
			bookRepository.save(book);
		}

		cartRepository.save(cart);
		user.addOrder(order);
		userRepository.save(user);
	}

	public User getUserDetails(int userid) {
		User user = userRepository.findById(userid).get();
		UserAddress address = user.getAddress();
		if (address == null) {
			address = new UserAddress();
			user.setAddress(address);
		}
		return user;
	}

	public User updateUserDetails(int userid, User updatedUser) {
		System.out.println("Updated User values : " + updatedUser);
		updateUserAddress(userid, updatedUser.getAddress());
		return updateAccountDetails(userid, updatedUser);
	}

	public Cart getBuyCart(int userid) {

		User user = userRepository.findById(userid).get();
		Cart cart = user.getCart();
		Cart buyCart = new Cart();

		cart.getCartItems().forEach(cartItem -> {
			BookItem bookItem = cartItem;

			if (cartItem.getBook().getStockQuantity() == 0) {
				return;
			}

			if (cartItem.getQuantity() > cartItem.getBook().getStockQuantity()) {
				bookItem = new BookItem(cartItem.getBook(), cartItem.getBook().getStockQuantity());
			}
			buyCart.addCartItem(bookItem);

		});

		return buyCart;

	}

}
