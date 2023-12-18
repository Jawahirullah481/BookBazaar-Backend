package com.jawa.bookbazaar.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.aspectj.weaver.ast.Var;
import org.hibernate.id.IntegralDataTypeHolder;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

//V
@Entity
@Table(name = "carts")
@JsonFilter("CartFilter")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_id")
	private int id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ElementCollection
	@JoinTable(name = "cart_items", joinColumns = { @JoinColumn(name = "cart_id") })
	private SortedSet<BookItem> cartItems = new TreeSet<BookItem>();
	

	@Column(name = "total_cost")
	private float totalCost;

	public Cart() {
		this.totalCost = 0.0f;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<BookItem> getCartItems() {
		return Collections.unmodifiableSet(this.cartItems);
	}

	public boolean addCartItem(BookItem newItem) {
		if(cartItems.add(newItem)) {
			float addedCost = newItem.getQuantity() * newItem.getBook().getPrice();
			setTotalCost(this.totalCost + addedCost);
			return true;
		}
		
		return false;
	}
	
	
	public boolean deleteCartItem(BookItem bookItem) {

		return cartItems.removeIf(bookItem1 -> {

			if (bookItem1.getBook().getIsbn() == bookItem.getBook().getIsbn())
				return true;

			return false;

		});
	}
	

	public float getTotalCost() {
		return totalCost;
	}

	private void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}

}
