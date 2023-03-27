package com.example.demo.model.word;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.demo.model.BaseEntity;
import com.example.demo.model.relgroup.RelGroup;

import jakarta.persistence.*;

@Entity
@Table(name = "word", indexes = {@Index(columnList="wordval"), @Index(columnList="count")})
public class Word extends BaseEntity{

	private String wordval;
	
	@JsonIgnore
	private long count;

	@JsonIgnore
	@ManyToMany(mappedBy="wordsInGroup")
	private Set<RelGroup> relgroup = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy="word1")
	private Set<Word2> word2 = new HashSet<>();

	public String getWordval() {
		return wordval;
	}

	public void setWordval(String wordval) {
		this.wordval = wordval;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Set<RelGroup> getRelgroup() {
		return relgroup;
	}

	public void setRelgroup(Set<RelGroup> relgroup) {
		this.relgroup = relgroup;
	}

	public Set<Word2> getWord2() {
		return word2;
	}

	public void setWord2(Set<Word2> word2) {
		this.word2 = word2;
	}

	
	
}