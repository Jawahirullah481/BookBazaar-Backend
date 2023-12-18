package com.jawa.bookbazaar.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jawa.bookbazaar.model.Order;
import com.jawa.bookbazaar.model.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	Page<Order> findByOrderStatusNot(OrderStatus orderStatusNot, Pageable pageable);
	List<Order> findByOrderStatusNot(OrderStatus delivered);
}
