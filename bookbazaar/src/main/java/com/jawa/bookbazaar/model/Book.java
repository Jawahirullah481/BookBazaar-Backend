package com.jawa.bookbazaar.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

//V
@Entity
@Table(name = "books")
@JsonFilter("BookFilter")
public class Book implements Comparable<Book> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
	private int id;

	@Min(value = 1000000000000l)
	@Max(value = 9999999999999l)
	@Column(unique = true)
	private long isbn;

	@Column(name = "book_name", unique = true)
	private String bookName;

	@Column(name = "stock")
	private int stockQuantity;

	@Column
	@DecimalMin(value = "0.00")
	private float price;

	private List<String> authors;

	@Column(length = 2000)
	private String description;

	private double rating;

	@Column(length = 500)
	private String imageUrl;

	public Book() {
		this.stockQuantity = 0;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public long getIsbn() {
		return isbn;
	}

	public void setIsbn(long isbn) {
		this.isbn = isbn;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public void addStockQuantity(int newQuantity) {
		this.stockQuantity += newQuantity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public int compareTo(Book o) {
		return (int)(this.isbn - o.getIsbn());
	}

}

//Book :
//	
//	Book ID     -    ISBN    -    Book Name   -   Quantity   -   Price
//	  1     	     1234	     xyxlskfj dkl		5				2
//	  2				 2343		 sldkfj lsdkfjs		8				3
//	
//	
//Order :
//	
//	Order ID    -    USER ID   -   Total Cost    -   Ordered Date    -    Delivered Date   
//	  1         -	 1		   -       230.00	      23.01.2003			24.10.2004
//	  2			     2		   -       150.00    	  15.02.2003			25.09.2002
//	
//	  
//OrderItem :
//	
//	OrderItem ID    -   Order ID    - 	Book ID		-	  Quantity   -    Cost
//	   1					1				2				4				234.00
//	   2					1				1		     	3				334.00
//
