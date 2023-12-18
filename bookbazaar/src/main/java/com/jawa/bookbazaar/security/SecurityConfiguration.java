package com.jawa.bookbazaar.security;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	// Authentication
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
	    return new MvcRequestMatcher.Builder(introspector);
	}
	
	
	// Authorization
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, MvcRequestMatcher.Builder mvc)throws Exception {
		
		DelegatingServerLogoutHandler logoutHandler = new DelegatingServerLogoutHandler(
	            new WebSessionServerLogoutHandler(), new SecurityContextServerLogoutHandler()
	    );
		
		return httpSecurity.authorizeHttpRequests(
							auth -> auth.requestMatchers(mvc.pattern("/admin/login")).permitAll()
							.requestMatchers(mvc.pattern("/users/signup"), mvc.pattern("/users/login"), mvc.pattern("/admin/login"), mvc.pattern("/h2-console/**"), mvc.pattern("/"), mvc.pattern("/books/**"), mvc.pattern("/v2/api-docs"), mvc.pattern("/v3/api-docs"), mvc.pattern("/configuration/ui"), mvc.pattern("/swagger-resources/**"), mvc.pattern("/configuration/security"), mvc.pattern("/swagger-ui.html"), mvc.pattern("/swagger-ui/index.html"), mvc.pattern("/webjars/**")).permitAll()
							.requestMatchers(toH2Console()).permitAll()
							.requestMatchers(mvc.pattern("/admin/**")).hasRole("ADMIN")
							.requestMatchers(mvc.pattern("/users/**")).hasAnyRole("ADMIN", "USER")
							.anyRequest().authenticated()	
							)
							.csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console()))
							.csrf(csrf -> csrf.disable())
							.cors(Customizer.withDefaults())
							.headers( header -> header.disable())
							.sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
							.httpBasic(Customizer.withDefaults())
							.logout(logout -> logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
							.build();
	}
	
	
	// Authentication Provider
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setPasswordEncoder(passwordEncoder());
		authProvider.setUserDetailsService(userDetailsService());
		return authProvider;
	}
	
	
	// Password Encoding
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				.allowedMethods("*")
				.allowedOriginPatterns("*")
				.allowedOrigins("http://localhost:3000");
			}
		};
	}
	
}
