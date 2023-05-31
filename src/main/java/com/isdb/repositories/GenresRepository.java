package com.isdb.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isdb.models.Genre;

@Repository
public interface GenresRepository extends JpaRepository<Genre, Long> {
	
	Genre findByName(String name);
	Optional<Genre> findById(Long id);
	
}
