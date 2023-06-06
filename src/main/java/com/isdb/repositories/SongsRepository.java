package com.isdb.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isdb.models.Song;

@Repository
public interface SongsRepository extends JpaRepository<Song, Long> {
	
	Song findByName(String name);
	Optional<Song> findById(Long id);
	
}
