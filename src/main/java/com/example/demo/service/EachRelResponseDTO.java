package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

public class EachRelResponseDTO {
	private String word1;
	private String word2;
	private List<Title> books = new ArrayList<>();
	public EachRelResponseDTO(String word1, String word2, List<Title> books) {
		super();
		this.word1 = word1;
		this.word2 = word2;
		this.books = books;
	}
	
    public static class Title {
        private String title;
        public Title(String title) {
        	this.setTitle(title);
        }
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
    }

	public String getWord1() {
		return word1;
	}

	public void setWord1(String word1) {
		this.word1 = word1;
	}

	public String getWord2() {
		return word2;
	}

	public void setWord2(String word2) {
		this.word2 = word2;
	}

	public List<Title> getBooks() {
		return books;
	}

	public void setBooks(List<Title> books) {
		this.books = books;
	}	
    
    
}
