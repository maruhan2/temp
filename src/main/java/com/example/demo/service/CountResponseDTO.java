package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

public class CountResponseDTO {
	private List<EachCountResponseDTO> pair = new ArrayList<>();

	public List<EachCountResponseDTO> getPair() {
		return pair;
	}

	public void setPair(List<EachCountResponseDTO> pair) {
		this.pair = pair;
	}

	public CountResponseDTO(List<EachCountResponseDTO> pair) {
		super();
		this.pair = pair;
	}
	
}
