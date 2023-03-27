package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

public class EachCountResponseDTO {
	private String word;
	private long count;
	private List<Relword> rel_words = new ArrayList<>();

    public EachCountResponseDTO(String word, long count, List<Relword> words) {
		super();
		this.word = word;
		this.count = count;
		this.rel_words = words;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<Relword> getRel_words() {
		return rel_words;
	}

	public void setRel_words(List<Relword> words) {
		this.rel_words = words;
	}

	public static class Relword {
        private String wordval;
        public Relword(String wordval) {
        	this.setWordval(wordval);
        }
		public String getWordval() {
			return wordval;
		}

		public void setWordval(String wordval) {
			this.wordval = wordval;
		}

    }
}
