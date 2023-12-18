package com.jawa.bookbazaar.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.jawa.bookbazaar.model.enums.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

//V
@Entity
@Table(name = "orders")
@JsonFilter("OrderFilter")
public class Order implements Comparable<Order> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@ElementCollection(fetch = FetchType.LAZY)
	@JoinTable(name = "order_items", joinColumns = { @JoinColumn(name = "order_id") })
	private List<BookItem> orderItems = new LinkedList<BookItem>();

	@Column(name = "total_cost")
	private float totalCost;

	@Column(name = "order_date")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate orderedDate;

	@Column(name = "deliver_date")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate deliveredDate;

	public Order() {
		orderStatus = OrderStatus.ORDERED;
		orderedDate = LocalDate.now();
		deliveredDate = null;
	}

	public int getId() {
		return this.id;
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

	public String getOrderStatus() {
		return orderStatus.toString();
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
		if(orderStatus == OrderStatus.DELIVERED) {
			deliveredDate = LocalDate.now();
		}
	}

	public List<BookItem> getOrderItems() {
		return Collections.unmodifiableList(this.orderItems);
	}

	public boolean addOrderItem(BookItem newItem) {
		
		
		boolean itemAlreadyAvailable = orderItems.stream().anyMatch( orderItem -> {
			
			if(orderItem.getBook().getIsbn() == newItem.getBook().getIsbn()) {
				return true;
			}
			
			return false;
		});
		
		
		if(!itemAlreadyAvailable) {
			orderItems.add(newItem);
			float addedCost = newItem.getQuantity() * newItem.getBook().getPrice();
			setTotalCost(this.totalCost + addedCost);
			return true;
		}
		
		return false;
	}

	public float getTotalcost() {
		return this.totalCost;
	}

	private void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}

	public LocalDate getOrderedDate() {
		return orderedDate;
	}

	public void setOrderedDate(LocalDate orderedDate) {
		this.orderedDate = orderedDate;
	}

	public LocalDate getDeliveredDate() {
		return deliveredDate;
	}

	public void setDeliveredDate(LocalDate deliveredDate) {
		this.deliveredDate = deliveredDate;
	}

	@Override
	public int compareTo(Order o) {
		return this.id - o.id;
	}

}