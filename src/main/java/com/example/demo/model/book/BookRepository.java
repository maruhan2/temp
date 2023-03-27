package com.example.demo.model.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;

import com.example.demo.model.relgroup.RelGroup;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
	boolean existsByIsbn(String isbn);
	Page<Book> findByTitleContaining(String title, PageRequest pageRequest);
	//Page<Book> findByTitleContaining(String title);
	Set<Book> findByTitleContaining(String title);
	Set<Book> findByIsbnContaining(String isbn);
}
