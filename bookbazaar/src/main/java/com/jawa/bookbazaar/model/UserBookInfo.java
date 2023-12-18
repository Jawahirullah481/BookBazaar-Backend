package com.jawa.bookbazaar.model;

import com.fasterxml.jackson.annotation.JsonFilter;


@JsonFilter("UserBookInfoFilter")
public class UserBookInfo {

	private Book book;
	private boolean inCart;
	private boolean inFavourite;
	private int stock;

	public UserBookInfo(Book book, boolean inCart, boolean inFavourite, int stock) {
		this.book = book;
		this.inCart = inCart;
		this.inFavourite = inFavourite;
		this.stock = stock;
	}
	

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public boolean isInCart() {
		return inCart;
	}

	public void setInCart(boolean inCart) {
		this.inCart = inCart;
	}

	public boolean isInFavourite() {
		return inFavourite;
	}

	public void setInFavourite(boolean inFavourite) {
		this.inFavourite = inFavourite;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

}
