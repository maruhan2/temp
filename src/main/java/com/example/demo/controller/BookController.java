package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.gson.Gson;
import com.example.demo.model.book.Book;
import com.example.demo.service.BookResponseDTO;
import com.example.demo.service.BookService;
import com.example.demo.service.CountResponseDTO;
import com.example.demo.service.RelResponseDTO;

@RestController
public class BookController {
	private static final Gson gson = new Gson();
	@Autowired
	private BookService bookService;
	
	
	@GetMapping("/book/collect")
	public HttpStatus collectBook(@RequestParam String query) {
		//Thread
		bookService.collectBook(query);
		return HttpStatus.OK;
	}
	
	@GetMapping("/book/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable long id) {
		return ResponseEntity.ok().body(bookService.getBookById(id));
	}	

	@GetMapping("/book")
	public ResponseEntity<Book> getBookByIsbn(@RequestParam String isbn) {
		return ResponseEntity.ok().body(bookService.getBookByIsbn(isbn));
	}	
	
	@PostMapping("/book")
	public ResponseEntity<Book> addWordByIsbn(@RequestBody Map<String,String> requestParams) {
		String isbn = requestParams.get("isbn");
		String word = requestParams.get("word");
		return ResponseEntity.ok().body(bookService.addWordByIsbn(isbn, word));
	}
	
	@DeleteMapping("/book")
	public ResponseEntity<Book> removeWordByIsbn(@RequestParam String isbn, @RequestParam String word){
		return ResponseEntity.ok().body(bookService.removeWordByIsbn(isbn, word));
	}	
	
	@GetMapping("/book/words")
	public ResponseEntity<MappingJacksonValue> getBookWords(@RequestParam String title, @RequestParam String type, @RequestParam int page, @RequestParam int size) {
		SimpleBeanPropertyFilter filter = null;
		if(type.equals("slice")) {
			 filter = SimpleBeanPropertyFilter.serializeAllExcept("totalCount");
		}
		else {
			 filter = SimpleBeanPropertyFilter.serializeAllExcept("");
		}

	    FilterProvider filters = new SimpleFilterProvider().addFilter("bfilter", filter);

	    MappingJacksonValue mapping = new MappingJacksonValue(bookService.getBookWords(title, type, page, size));

	    mapping.setFilters(filters);

		return ResponseEntity.ok().body(mapping);
	}
	
	@GetMapping("/book/wordpair")
	public ResponseEntity<List<Book>> getRel(@RequestParam String w1, @RequestParam String w2){
		return ResponseEntity.ok().body(bookService.getRel(w1, w2));
	}
	
	@GetMapping("/book/toprel")
	public ResponseEntity<RelResponseDTO> getTopRel(){
		return ResponseEntity.ok().body(bookService.getTopRel());
	}	

	@GetMapping("/book/topcount")
	public ResponseEntity<CountResponseDTO> getTopCount(){
		return ResponseEntity.ok().body(bookService.getTopCount());
	}	
	
}