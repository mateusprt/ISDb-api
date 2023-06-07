package com.isdb.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isdb.dtos.artists.CreateOrUpdateArtistDto;
import com.isdb.dtos.artists.ResponseArtistDto;
import com.isdb.dtos.genre.ResponseGenreDto;
import com.isdb.exceptions.ResourceAlreadyExistsException;
import com.isdb.exceptions.ResourceNotFoundException;
import com.isdb.models.Artist;
import com.isdb.models.Genre;
import com.isdb.repositories.ArtistsRepository;
import com.isdb.repositories.GenresRepository;
import com.isdb.services.interfaces.ArtistsServiceInterface;

@Service
public class ArtistsService implements ArtistsServiceInterface {
	
	private static Logger log = Logger.getLogger(ArtistsService.class.getName());
	
	@Autowired
	private ArtistsRepository artistsRepository;
	
	@Autowired
	private GenresRepository genresRepository;

	@Override
	public ResponseArtistDto createArtist(CreateOrUpdateArtistDto dto) {
		log.info("Finding genre by id = " + dto.getGenreId());
		Genre genreFound = this.genresRepository.findById(dto.getGenreId()).orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
		log.info("Genre found = " + genreFound);
		
		log.info("Finding artist by name = " + dto.getName());
		Artist artistFound = this.artistsRepository.findByName(null);
		
		if(artistFound != null) {
			log.info("Artist found = " + artistFound);
			throw new ResourceAlreadyExistsException("Artist already exists");
		}
		
		Artist artistToBeSaved = new Artist();
		artistToBeSaved.setGenre(genreFound);
		artistToBeSaved.setName(dto.getName());
		artistToBeSaved.setBirthDate(dto.getBirthDate());
		artistToBeSaved.setDescription(dto.getDescription());
		log.info("Saving genre");
		Artist artistSaved = this.artistsRepository.save(artistToBeSaved);
		log.info("Genre Saved: " + artistSaved);
		ResponseArtistDto response = new ResponseArtistDto(
				artistSaved.getId(),
				new ResponseGenreDto(artistSaved.getGenre().getId(), artistSaved.getGenre().getName()),
				artistSaved.getName(),
				artistSaved.getBirthDate(),
				artistSaved.getDescription());
		return response;
	}

	@Override
	public ResponseArtistDto getArtist(Long id) {
		log.info("Finding artist by id = " + id);
		Artist artistFound = this.artistsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		log.info("Artist found" + artistFound);
		ResponseArtistDto dto = new ResponseArtistDto(
				artistFound.getId(),
				new ResponseGenreDto(artistFound.getGenre().getId(), artistFound.getGenre().getName()),
				artistFound.getName(), artistFound.getBirthDate(), artistFound.getDescription()); 
		return dto;
	}

	@Override
	public ResponseArtistDto updateArtist(Long id, CreateOrUpdateArtistDto dto) {
		log.info("Finding artist by id = " + id);
		Artist artistFound = this.artistsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		log.info("Artist found" + artistFound);
		artistFound.setName(dto.getName());
		artistFound.setBirthDate(dto.getBirthDate());
		artistFound.setDescription(dto.getDescription());
		log.info("Updating artist");
		Artist artistSaved = this.artistsRepository.save(artistFound);
		log.info("Artist updated successfully = " + artistSaved);
		ResponseArtistDto response = new ResponseArtistDto(
				artistFound.getId(), 
				new ResponseGenreDto(artistSaved.getGenre().getId(), artistSaved.getGenre().getName()),
				artistSaved.getName(),
				artistSaved.getBirthDate(),
				artistSaved.getDescription());
		return response;
	}

	@Override
	public void deleteArtist(Long id) {
		log.info("Finding artist by id = " + id);
		Artist artistFound = this.artistsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		log.info("Artist found" + artistFound);
		this.artistsRepository.delete(artistFound);
		
	}

	@Override
	public List<ResponseArtistDto> getAllArtists() {
		log.info("Getting all artists");
		List<Artist> artists = this.artistsRepository.findAll();
		log.info("Artists found = " + artists.size());
		List<ResponseArtistDto> dtos = new ArrayList<>();
		artists.forEach(artist -> {
			ResponseArtistDto newDto = new ResponseArtistDto(
					artist.getId(), 
					new ResponseGenreDto(artist.getGenre().getId(), artist.getGenre().getName()),
					artist.getName(),
					artist.getBirthDate(),
					artist.getDescription());
			dtos.add(newDto);
		});
		return dtos;
	}

	

}
