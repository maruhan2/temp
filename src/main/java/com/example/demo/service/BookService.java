package com.example.demo.service;

import com.example.demo.model.book.*;
import com.example.demo.model.book.KakaoBook.Document;

import java.util.List;

public interface BookService {
	Book getBookById(long bookId);
	void collectBook(String query);
	void addBookFromDocument(Document d);
	void addWord(Book book, String word);
	void removeWord(Book book, String word);
	BookResponseDTO getBookWords(String title, String type, int page, int size);
	Book getBookByIsbn(String isbn);
	Book addWordByIsbn(String isbn, String word);
	Book removeWordByIsbn(String isbn, String word);
	List<Book> getRel(String w1, String w2);
	RelResponseDTO getTopRel();
	CountResponseDTO getTopCount();
}
