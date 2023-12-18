package com.jawa.bookbazaar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class BookbazaarApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookbazaarApplication.class, args);
	}

}
