package com.example.demo.model.relgroup;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelGroupRepository extends JpaRepository<RelGroup, Long>{

	Set<RelGroup> findByFirstletterAndBookIdAndFilled(String firstLetter, long id, boolean filled);

	List<RelGroup> findByBookIdOrderByFirstletter(Long id);
}
