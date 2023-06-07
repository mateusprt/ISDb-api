package com.isdb.controllers;

import java.util.List;
import java.util.logging.Logger;

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
	
	private static final Logger logger = Logger.getLogger(GenresController.class.toString());
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<ResponseGenreDto>> getAllGenres() {
		logger.info("Get all genres");
		List<ResponseGenreDto> responseDto = this.genreServices.getAllGenres();
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ResponseGenreDto> createGenre(@RequestBody CreateOrUpdateGenreDto dto) {
		logger.info("Create genre");
		ResponseGenreDto responseDto = this.genreServices.createGenre(dto);
		logger.info("Genre created successfully");
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseGenreDto> getGenre(@PathVariable(value = "id") Long id) {
		logger.info("Get a genre");
		ResponseGenreDto responseDto = this.genreServices.getGenre(id);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseGenreDto> updateGenre(@PathVariable(value = "id") Long id, @RequestBody CreateOrUpdateGenreDto dto) {
		logger.info("Update genre with id = " + id);
		ResponseGenreDto responseDto = this.genreServices.updateGenre(id, dto);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Void> deleteGenre(@PathVariable(value = "id") Long id) {
		logger.info("Delete genre with id = " + id);
		this.genreServices.deleteGenre(id);
		return ResponseEntity.ok().build();
	}
	
}
