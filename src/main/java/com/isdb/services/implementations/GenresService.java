package com.isdb.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isdb.dtos.genre.CreateOrUpdateGenreDto;
import com.isdb.dtos.genre.ResponseGenreDto;
import com.isdb.exceptions.ResourceAlreadyExistsException;
import com.isdb.exceptions.ResourceNotFoundException;
import com.isdb.mappers.ApplicationMapper;
import com.isdb.models.Genre;
import com.isdb.repositories.GenresRepository;
import com.isdb.services.interfaces.GenresServiceInterface;

@Service
public class GenresService implements GenresServiceInterface {
	
	@Autowired
	private GenresRepository genresRepository;
	
	private Logger log = Logger.getLogger(GenresService.class.getName());

	@Override
	public ResponseGenreDto createGenre(CreateOrUpdateGenreDto dto) {
		log.info("Finding genre by name: [" + dto.getName() + "]");
		Genre genreFound = this.genresRepository.findByName(dto.getName());
		
		if(genreFound != null) {
			log.warning("Genre found");
			throw new ResourceAlreadyExistsException("Genre already exists");
		}
		
		log.info("Genre not found");
		
		Genre genreToBeSaved = ApplicationMapper.mapObject(dto, Genre.class);
		log.info("Saving genre");
		ResponseGenreDto response = ApplicationMapper.mapObject(this.genresRepository.save(genreToBeSaved), ResponseGenreDto.class);
		log.info("Genre Saved");
		return response;
	}

	@Override
	public ResponseGenreDto getGenre(Long id) {
		log.info("Finding genre by id: [" + id + "]");
		Genre genreFound = this.genresRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
		log.info("Genre found");
		ResponseGenreDto dto = ApplicationMapper.mapObject(genreFound, ResponseGenreDto.class);
		return dto;
	}

	@Override
	public ResponseGenreDto updateGenre(Long id, CreateOrUpdateGenreDto dto) {
		log.info("Finding genre by id: [" + id + "]");
		Genre genreFound = this.genresRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
		
		log.info("Genre found");
		genreFound.setName(dto.getName());
		
		log.info("Updating genre");
		ResponseGenreDto response = ApplicationMapper.mapObject(this.genresRepository.save(genreFound), ResponseGenreDto.class);
		log.info("Genre updated");
		return response;
	}

	@Override
	public void deleteGenre(Long id) {
		log.info("Finding genre by id: [" + id + "]");
		Genre genreFound = this.genresRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
		log.info("Genre found");
		
		log.info("Deleting genre");
		this.genresRepository.delete(genreFound);
		log.info("Genre deleted");
	}

	@Override
	public List<ResponseGenreDto> getAllGenres() {
		List<Genre> genres = this.genresRepository.findAll();
		List<ResponseGenreDto> dtos = new ArrayList<>();
		genres.forEach(genre -> dtos.add(ApplicationMapper.mapObject(genre, ResponseGenreDto.class)));
		return dtos;
	}

}
