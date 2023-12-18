package com.jawa.bookbazaar.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

//V
@Embeddable
public class BookItem implements Comparable<BookItem> {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "book_id")
	private Book book;

	private int quantity;

	private float totalCost;

	public BookItem() {
		this.quantity = 0;
		this.totalCost = 0.0f;
	}

	public BookItem(Book book) {
		this.book = book;
		this.quantity = 1;
		this.totalCost = book.getPrice() * quantity;
	}

	public BookItem(Book book, int quantity) {
		this.book = book;
		this.quantity = quantity;
		this.totalCost = book.getPrice() * quantity;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getTotalCost() {
		totalCost = book.getPrice() * quantity;
		return totalCost;
	}

	@Override
	public int compareTo(BookItem booItem) {
		return (int) (this.getBook().getIsbn() - booItem.getBook().getIsbn());
	}

	@Override
	public String toString() {
		return "BookItem [book=" + book.getIsbn() + ", quantity=" + quantity + ", totalCost=" + totalCost + "]";
	}

}