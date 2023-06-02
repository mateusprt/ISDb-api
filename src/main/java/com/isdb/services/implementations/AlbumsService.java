package com.isdb.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isdb.dtos.albums.CreateOrUpdateAlbumDto;
import com.isdb.dtos.albums.ResponseAlbumDto;
import com.isdb.exceptions.ResourceAlreadyExistsException;
import com.isdb.exceptions.ResourceNotFoundException;
import com.isdb.models.Album;
import com.isdb.models.Artist;
import com.isdb.repositories.AlbumsRepository;
import com.isdb.repositories.ArtistsRepository;
import com.isdb.services.interfaces.AlbumsServiceInterface;

@Service
public class AlbumsService implements AlbumsServiceInterface {
	
	@Autowired
	private ArtistsRepository artistsRepository;
	
	@Autowired
	private AlbumsRepository albumsRepository;
	
	private Logger log = Logger.getLogger(ArtistsService.class.getName());

	@Override
	public ResponseAlbumDto createAlbum(Long artistId, CreateOrUpdateAlbumDto dto) {
		log.info("Finding artist by id: [" + artistId + "]");
		Artist artistFound = this.artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		log.info("Artist found: " + artistFound);
		
		log.info("Finding album by name: [" + dto.getName() + "]");
		Album albumFound = this.albumsRepository.findByName(dto.getName());
		
		if(albumFound != null) {
			log.warning("Album found: " + artistFound);
			throw new ResourceAlreadyExistsException("Album already exists");
		}
		
		Album albumToBeSaved = new Album();
		albumToBeSaved.setArtist(artistFound);
		albumToBeSaved.setName(dto.getName());
		albumToBeSaved.setReleaseDate(dto.getReleaseDate());
		log.info("Saving album");
		Album albumSaved = this.albumsRepository.save(albumToBeSaved);
		log.info("Album Saved: " + albumSaved);
		ResponseAlbumDto response = new ResponseAlbumDto(albumSaved.getId(), albumSaved.getName(), albumSaved.getReleaseDate());
		return response;
	}

	@Override
	public ResponseAlbumDto getAlbum(Long artistId, Long albumId) {
		Artist artistFound = this.artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		Album albumFound = null;
		
		if(artistFound.albums.size() > 0) {
			for(Album currentAlbum : artistFound.albums) {
				if(currentAlbum.getId().equals(albumId)) {
					albumFound = currentAlbum;
				}
			}
		}
		
		if(albumFound == null) {
			throw new ResourceNotFoundException("Album not found");
		}
		
		ResponseAlbumDto dto = new ResponseAlbumDto(albumFound.getId(), albumFound.getName(), albumFound.getReleaseDate());
		return dto;
	}

	@Override
	public ResponseAlbumDto updateAlbum(Long artistId, Long albumId, CreateOrUpdateAlbumDto dto) {
		Artist artistFound = this.artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		Album albumFound = null;
		
		if(artistFound.albums.size() > 0) {
			for(Album currentAlbum : artistFound.albums) {
				if(currentAlbum.getId().equals(albumId)) {
					albumFound = currentAlbum;
				}
			}
		}
		
		if(albumFound == null) {
			throw new ResourceNotFoundException("Album not found");
		}
				
		albumFound.setName(dto.getName());
		albumFound.setReleaseDate(dto.getReleaseDate());
		
		Album albumSaved = this.albumsRepository.save(albumFound);
		ResponseAlbumDto response = new ResponseAlbumDto(albumSaved.getId(), albumSaved.getName(), albumSaved.getReleaseDate());
		return response;
	}

	@Override
	public void deleteAlbum(Long artistId, Long albumId) {
		Artist artistFound = this.artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		Album albumFound = null;
		
		if(artistFound.albums.size() > 0) {
			for(Album currentAlbum : artistFound.albums) {
				if(currentAlbum.getId().equals(albumId)) {
					albumFound = currentAlbum;
				}
			}
		} 
		
		if(albumFound == null) {
			throw new ResourceNotFoundException("Album not found");
		}
		this.albumsRepository.delete(albumFound);
	}

	@Override
	public List<ResponseAlbumDto> getAllAlbumsOfAnArtist(Long artistId) {
		Artist artistFound = this.artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		List<Album> albums = artistFound.getAlbums();
		List<ResponseAlbumDto> dtos = new ArrayList<>();
		albums.forEach(album -> {
			ResponseAlbumDto newDto = new ResponseAlbumDto(album.getId(), album.getName(), album.getReleaseDate());
			dtos.add(newDto);
		});
		return dtos;
	}

}
