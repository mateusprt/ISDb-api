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

import com.isdb.dtos.genre.CreateOrUpdateGenreDto;
import com.isdb.dtos.genre.ResponseGenreDto;
import com.isdb.services.interfaces.GenresServiceInterface;

@RestController
@RequestMapping("/api/v1/genres")
public class GenresController {
	
	@Autowired
	private GenresServiceInterface genreServices;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<ResponseGenreDto>> getAllGenres() {
		List<ResponseGenreDto> responseDto = this.genreServices.getAllGenres();
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ResponseGenreDto> createGenre(@RequestBody CreateOrUpdateGenreDto dto) {
		ResponseGenreDto responseDto = this.genreServices.createGenre(dto);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseGenreDto> getGenre(@PathVariable(value = "id") Long id) {
		ResponseGenreDto responseDto = this.genreServices.getGenre(id);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseGenreDto> updateGenre(@PathVariable(value = "id") Long id, @RequestBody CreateOrUpdateGenreDto dto) {
		ResponseGenreDto responseDto = this.genreServices.updateGenre(id, dto);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseGenreDto> deleteGenre(@PathVariable(value = "id") Long id) {
		this.genreServices.deleteGenre(id);
		return ResponseEntity.ok().build();
	}
	
}
