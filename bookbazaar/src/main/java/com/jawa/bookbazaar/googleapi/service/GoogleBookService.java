package com.jawa.bookbazaar.googleapi.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jawa.bookbazaar.exception.InvalidBookIsbnException;
import com.jawa.bookbazaar.exception.InvalidBookNameException;
import com.jawa.bookbazaar.googleapi.model.BookVolume;
import com.jawa.bookbazaar.googleapi.model.BookVolume.BookVolumeItem;
import com.jawa.bookbazaar.googleapi.model.BookVolume.BookVolumeItem.ImageLinks;
import com.jawa.bookbazaar.googleapi.model.BookVolume.BookVolumeItem.IndustryIdentifier;
import com.jawa.bookbazaar.model.Book;

@Service
public class GoogleBookService {

	private static final String API_KEY = "&key=AIzaSyC0R2vypAiUuPhBJXoaWhPDpN5fl1o5lZE";
	private static final String MAX_RESULTS = "&maxResults=10";
	private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";

	// https://www.googleapis.com/books/v1/volumes?q=isbn:9780143454212&key=AIzaSyC0R2vypAiUuPhBJXoaWhPDpN5fl1o5lZE

	public Book getBookByIsbn(long isbn) {

		BookVolume bookVolume;
		try {
			bookVolume = requestQuery(BASE_URL + "isbn:" + isbn);
		} catch (Exception e) {
			throw new InvalidBookIsbnException(String.format("No Book Available for the isbn %d", isbn));
		}
		BookVolumeItem bookInfo = bookVolume.items().get(0);

		Book book = new Book();
		book.setIsbn(isbn);
		book.setBookName(bookInfo.volumeInfo().title());
		book.setPrice(0);
		book.setStockQuantity(0);
		book.setAuthors(bookInfo.volumeInfo().authors());
		if (bookInfo.volumeInfo().description().length() > 2000) {
			book.setDescription(bookInfo.volumeInfo().description().substring(0, 1900));
		} else {
			book.setDescription(bookInfo.volumeInfo().description());
		}
		book.setRating(bookInfo.volumeInfo().averageRating());

		ImageLinks imageLinks = bookInfo.volumeInfo().imageLinks();

		book.setImageUrl((imageLinks != null) ? (imageLinks.thumbnail().replace("&edge=curl", "")) : "Image not available for this book");

		return book;
	}

	public List<Book> searchByName(String bookname) throws InvalidBookNameException {

		if (bookname.equals("")) {
			throw new InvalidBookNameException("Book Name cannot be empty");
		}

		BookVolume bookVolume = requestQuery(BASE_URL + tokenize(bookname.trim()) + MAX_RESULTS);
		List<Book> books = getBooksFromBookVolume(bookVolume);
		return books;

	}

	public List<Book> filter(String searchText, String author, String subject) throws InvalidBookNameException {

		StringBuffer query = new StringBuffer("");
		query.append(BASE_URL);

		if (!searchText.equals("null")) {
			// throw new InvalidBookNameException("Book Name cannot be empty");
			query.append(tokenize(searchText));
		}

		if (!author.equals("")) {
			query.append("&inauthor:");
			query.append(tokenize(author));
		}

		if (!subject.equals("")) {
			query.append("&subject:");
			query.append(tokenize(subject));
		}

		query.append(MAX_RESULTS);


		BookVolume bookVolume = requestQuery(query.toString());
		return getBooksFromBookVolume(bookVolume);
	}

	private List<Book> getBooksFromBookVolume(BookVolume bookVolume) {

		List<Book> books = new ArrayList<>();

		for (BookVolumeItem bookVolumeItem : bookVolume.items()) {
			Book book = new Book();

			List<IndustryIdentifier> industryIdentifiers = bookVolumeItem.volumeInfo().industryIdentifiers();

			if (industryIdentifiers == null) {
				continue;
			}

			for (IndustryIdentifier identifier : industryIdentifiers) {
				if (identifier.type().equals("ISBN_13")) {
					book.setIsbn(Long.parseLong(identifier.identifier()));
				}
			}

			book.setBookName(bookVolumeItem.volumeInfo().title());
			book.setPrice(0);
			book.setStockQuantity(0);
			book.setAuthors(bookVolumeItem.volumeInfo().authors());
			book.setDescription(bookVolumeItem.volumeInfo().description());
			book.setRating(bookVolumeItem.volumeInfo().averageRating());

			ImageLinks imageLinks = bookVolumeItem.volumeInfo().imageLinks();
			
			book.setImageUrl((imageLinks != null) ? (imageLinks.smallThumbnail().replace("&edge=curl", "")) : "Image not available for this book");

			books.add(book);
		}

		return books;

	}

	private BookVolume requestQuery(String queryString) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<BookVolume> responseEntity = restTemplate.getForEntity(URI.create(queryString + API_KEY),
				BookVolume.class);

		HttpStatusCode statusCode = responseEntity.getStatusCode();
		BookVolume bookVolume = Objects.requireNonNull(responseEntity.getBody());

		if ((!statusCode.is2xxSuccessful()) || bookVolume.items().isEmpty()) {
			throw new InvalidBookIsbnException("Book not found for particular isbn");
		}

		return bookVolume;
	}

	private String tokenize(String str) {
		return str.replaceAll("\\s", "+");
	}

}
