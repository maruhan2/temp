package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

public class RelResponseDTO {
	private List<EachRelResponseDTO> pair = new ArrayList<>();

	public RelResponseDTO(List<EachRelResponseDTO> pair) {
		super();
		this.pair = pair;
	}

	public List<EachRelResponseDTO> getPair() {
		return pair;
	}

	public void setPair(List<EachRelResponseDTO> pair) {
		this.pair = pair;
	}

}
