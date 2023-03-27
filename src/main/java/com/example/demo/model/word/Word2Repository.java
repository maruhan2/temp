package com.example.demo.model.word;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Word2Repository extends JpaRepository<Word2, Long>{
	Set<Word2> findByWordval(String wordval);

	Set<Word2> findTop10ByOrderByCountDesc();
}