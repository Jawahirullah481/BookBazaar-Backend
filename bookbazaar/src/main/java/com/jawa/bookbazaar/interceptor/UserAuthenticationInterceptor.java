package com.jawa.bookbazaar.interceptor;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties.Token;
import org.springframework.data.domain.Window;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.jawa.bookbazaar.exception.UnauthenticatedUserException;
import com.jawa.bookbazaar.repository.UserRepository;
import com.jawa.bookbazaar.security.UserAuthDetails;
import com.jawa.bookbazaar.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserAuthenticationInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserAuthDetails userDetails = (UserAuthDetails)authentication.getPrincipal();
		System.out.println("Requested User is : " + userDetails.getUserId());
		int loggedUserId = userDetails.getUserId();
		int requestedUserId = 0;
				
		try {
			requestedUserId = Integer.parseInt(request.getRequestURI().split("/")[2]);
		}catch(NumberFormatException ex) {
			throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), null);
		}
		
		if(loggedUserId != requestedUserId) {
			return false;
		}
		
		
		return true;
	}
	
}
