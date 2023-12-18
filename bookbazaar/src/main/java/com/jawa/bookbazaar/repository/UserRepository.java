package com.jawa.bookbazaar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jawa.bookbazaar.model.User;
import com.jawa.bookbazaar.model.enums.UserRole;


@Repository
public interface UserRepository  extends JpaRepository<User, Integer>, CrudRepository<User, Integer>{
	
	Optional<User> findByUsername(String username);
	Optional<User> findByUsernameOrEmail(String username, String email);
	Optional<User> findByRole(UserRole role);
	boolean existsByUsername(String username);
	boolean existsByPassword(String password);
	boolean existsByEmail(String email);
	boolean existsByUsernameAndPassword(String username, String password);
	boolean existsByEmailAndPassword(String email, String password);
	User getByUsernameOrEmail(String username, String email);	
}
