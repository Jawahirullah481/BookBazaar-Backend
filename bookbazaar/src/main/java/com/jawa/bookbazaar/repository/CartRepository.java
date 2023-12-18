package com.jawa.bookbazaar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jawa.bookbazaar.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

}
