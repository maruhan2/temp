package com.example.demo.model.word;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long>{
	Set<Word> findByWordval(String wordval);

	Set<Word> findTop10ByOrderByCountDesc();
}
