package com.example.demo.model.relgroup;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.demo.model.BaseEntity;
import com.example.demo.model.book.Book;
import com.example.demo.model.word.Word;
import com.example.demo.model.word.Word2;

import jakarta.persistence.*;

@Entity
@Table(name = "relgroup", indexes = @Index(columnList="firstletter"))
public class RelGroup extends BaseEntity {
	
	private String firstletter;
	
	@JsonIgnore
	private boolean filled;
	
	@JsonIgnore	
	@ManyToOne
	private Book book;
	
	@JsonIgnore	
	@ManyToMany(mappedBy="relgroup")
	private Set<Word2> word2 = new HashSet<>();
	

	@ManyToMany
	@JoinTable(
			name="relgroup_word",
			joinColumns = @JoinColumn(name = "word_id"),
			inverseJoinColumns = @JoinColumn(name = "relgroup_id")
	)
	private Set<Word> wordsInGroup = new HashSet<>();

	public String getFirstLetter() {
		return firstletter;
	}

	public void setFirstLetter(String firstletter) {
		this.firstletter = firstletter;
	}

	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Set<Word> getWord() {
		return wordsInGroup;
	}

	public void setWord(Set<Word> word) {
		this.wordsInGroup = word;
	}

	public Set<Word2> getWord2() {
		return word2;
	}

	public void setWord2(Set<Word2> word2) {
		this.word2 = word2;
	}



}
