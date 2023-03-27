package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("bfilter")
public class BookResponseDTO {
    private long page;
    
    private long total_count;
    
    private boolean is_end;
    
    private List<EachBookResponseDTO> bookList= new ArrayList<>();
	public BookResponseDTO(long page, long total_count, boolean is_end, List<EachBookResponseDTO> bookList) {
		super();
		this.page = page;
		this.total_count = total_count;
		this.is_end = is_end;
		this.bookList = bookList;
	}
	public long getPage() {
		return page;
	}
	public void setPage(long page) {
		this.page = page;
	}
	public long getTotalCount() {
		return total_count;
	}
	public void setTotalCount(long total_count) {
		this.total_count = total_count;
	}
	public boolean isIs_end() {
		return is_end;
	}
	public void setIs_end(boolean is_end) {
		this.is_end = is_end;
	}
	public List<EachBookResponseDTO> getBookList() {
		return bookList;
	}
	public void setBookList(List<EachBookResponseDTO> bookList) {
		this.bookList = bookList;
	}

}