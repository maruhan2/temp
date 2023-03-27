package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

public class EachBookResponseDTO {
    private String title;
    private String isbn;
    private List<String> words= new ArrayList<>();
	public EachBookResponseDTO(String title, String isbn, List<String> words) {
		super();
		this.title = title;
		this.isbn = isbn;
		this.words = words;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public List<String> getWords() {
		return words;
	}
	public void setWords(List<String> words) {
		this.words = words;
	}
    
}
