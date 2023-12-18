package com.jawa.bookbazaar.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jawa.bookbazaar.model.User;
import com.jawa.bookbazaar.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			
		Optional<User> user = userRepository.findByUsernameOrEmail(username, username);

		user.orElseThrow( () -> new UsernameNotFoundException("User with credentials not found"));
		
		UserDetails userDetails = new UserAuthDetails(user.get());
		
		return userDetails;
	}

}
