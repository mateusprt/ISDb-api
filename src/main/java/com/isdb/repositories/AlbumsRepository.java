package com.isdb.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isdb.models.Album;
import com.isdb.models.Artist;

@Repository
public interface AlbumsRepository extends JpaRepository<Album, Long> {
	
	Album findByName(String name);
	Optional<Album> findById(Long id);
	
}
