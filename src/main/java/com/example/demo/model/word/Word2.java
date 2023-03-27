package com.example.demo.model.word;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.model.BaseEntity;
import com.example.demo.model.relgroup.RelGroup;

import jakarta.persistence.*;

@Entity
@Table(name = "word2", indexes = {@Index(columnList="wordval"), @Index(columnList="count")})
public class Word2 extends BaseEntity{

	private String wordval;
	private long count;

	@ManyToOne
	private Word word1;
	
	@ManyToMany
	@JoinTable(
			name="word2_relgroup",
			joinColumns = @JoinColumn(name = "relgroup_id"),
			inverseJoinColumns = @JoinColumn(name = "word2_id")
	)	
	private Set<RelGroup> relgroup = new HashSet<>();
	

	public Set<RelGroup> getRelgroup() {
		return relgroup;
	}

	public void setRelgroup(Set<RelGroup> relgroup) {
		this.relgroup = relgroup;
	}

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

	public Word getWord1() {
		return word1;
	}

	public void setWord1(Word word1) {
		this.word1 = word1;
	}
	
	
}