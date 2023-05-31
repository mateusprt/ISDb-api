package com.isdb.services.interfaces;

import java.util.List;

import com.isdb.dtos.genre.CreateOrUpdateGenreDto;
import com.isdb.dtos.genre.ResponseGenreDto;

public interface GenresServiceInterface {
	
	public ResponseGenreDto createGenre(CreateOrUpdateGenreDto dto);
	public ResponseGenreDto getGenre(Long id);
	public ResponseGenreDto updateGenre(Long id, CreateOrUpdateGenreDto dto);
	public void deleteGenre(Long id);
	public List<ResponseGenreDto> getAllGenres();
	
}
