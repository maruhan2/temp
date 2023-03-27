package com.example.demo.model.book;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.demo.model.BaseEntity;
import com.example.demo.model.relgroup.RelGroup;

import jakarta.persistence.*;

@Entity
@Table(name = "book", indexes = {@Index(columnList="title"), @Index(columnList="isbn")})
public class Book extends BaseEntity {
	
	private String title;
	private String isbn;
	
	@OneToMany(mappedBy="book")
	private Set<RelGroup> relgroup = new HashSet<>();
	
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<RelGroup> getRelgroup() {
		return relgroup;
	}

	public void setRelgroup(Set<RelGroup> relgroup) {
		this.relgroup = relgroup;
	}
	

}