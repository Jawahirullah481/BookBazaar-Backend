package com.jawa.bookbazaar.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.jawa.bookbazaar.model.enums.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// V
@Entity
@Table(name = "users")
@JsonFilter("UserFilter")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int id;

	@Column(unique = true)
	@Size(min = 3, message = "username must contain atleast 3 characters")
	@Pattern(regexp = "^[A-Za-z]+$", message = "username must contain only alphabets")
	@NotNull
	private String username;

	@Column(unique = true)
	@Email(message = "Enter valid email address")
	@NotNull
	@NotEmpty
	private String email;

	@Size(min = 5, message = "password must be contain 5 characters")
	@NotNull
	@NotEmpty
	private String password;
		
	@Enumerated(EnumType.STRING)
	private UserRole role;

	@OneToOne(cascade = CascadeType.ALL)
	private UserAddress address;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Order> orders = new HashSet<Order>();

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Cart cart = new Cart();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "favourites", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
	private SortedSet<Book> favourites = new TreeSet<Book>();

	public User() {
		this.cart.setUser(this);
		this.role = UserRole.USER;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
		return this.role.toString();
	}
	
	public void setRoleAsUser() {
		this.role = UserRole.USER;
	}
	
	public void setRoleAsAdmin() {
		this.role = UserRole.ADMIN;
	}
	
	public UserAddress getAddress() {
		return address;
	}

	public void setAddress(UserAddress address) {
		this.address = address;
	}

	public Set<Order> getOrders() {
		return Collections.unmodifiableSet(this.orders);
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public void addOrder(Order order) {
		this.orders.add(order);
		order.setUser(this);
	}

	public Set<Book> getFavourites() {
		return Collections.unmodifiableSet(this.favourites);
	}

	public boolean addToFavourite(Book book) {
		return favourites.add(book);
	}

	public Cart getCart() {
		return this.cart;
	}

	public boolean addToCart(BookItem bookItem) {
		
		return cart.addCartItem(bookItem);
		
	}

	public boolean deleteFromFavourites(long isbn) {

		return favourites.removeIf(book -> {

			if (book.getIsbn() == isbn)
				return true;

			return false;

		});
		
	}

	public boolean deleteFromCart(BookItem bookItem) {
		
		return cart.deleteCartItem(bookItem);
		
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", role="
				+ role + ", address=" + address + "]";
	}

}