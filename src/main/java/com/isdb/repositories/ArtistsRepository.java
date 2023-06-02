package com.isdb.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isdb.models.Artist;

@Repository
public interface ArtistsRepository extends JpaRepository<Artist, Long> {
	
	Artist findByName(String name);
	Optional<Artist> findById(Long id);

}
