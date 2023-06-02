package com.isdb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.isdb.dtos.artists.CreateOrUpdateArtistDto;
import com.isdb.dtos.artists.ResponseArtistDto;
import com.isdb.dtos.genre.CreateOrUpdateGenreDto;
import com.isdb.dtos.genre.ResponseGenreDto;
import com.isdb.services.interfaces.ArtistsServiceInterface;

@RestController
@RequestMapping("/api/v1/artists")
public class ArtistsController {
	
	@Autowired
	private ArtistsServiceInterface artistsService;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<ResponseArtistDto>> getAllGenres() {
		List<ResponseArtistDto> responseDto = this.artistsService.getAllArtists();
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ResponseArtistDto> createGenre(@RequestBody CreateOrUpdateArtistDto dto) {
		ResponseArtistDto responseDto = this.artistsService.createArtist(dto);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseArtistDto> getGenre(@PathVariable(value = "id") Long id) {
		ResponseArtistDto responseDto = this.artistsService.getArtist(id);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseArtistDto> updateGenre(@PathVariable(value = "id") Long id, @RequestBody CreateOrUpdateArtistDto dto) {
		ResponseArtistDto responseDto = this.artistsService.updateArtist(id, dto);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Void> deleteGenre(@PathVariable(value = "id") Long id) {
		this.artistsService.deleteArtist(id);
		return ResponseEntity.ok().build();
	}
	
}
