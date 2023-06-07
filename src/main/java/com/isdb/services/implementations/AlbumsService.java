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
	
	private  static Logger log = Logger.getLogger(AlbumsService.class.getName());
	
	@Autowired
	private ArtistsRepository artistsRepository;
	
	@Autowired
	private AlbumsRepository albumsRepository;


	@Override
	public ResponseAlbumDto createAlbum(Long artistId, CreateOrUpdateAlbumDto dto) {
		log.info("Finding artist by id = " + artistId);
		Artist artistFound = this.artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		log.info("Artist found: " + artistFound);
		
		log.info("Finding album by name = " + dto.getName());
		Album albumFound = this.albumsRepository.findByName(dto.getName());
		
		if(albumFound != null) {
			log.warning("Album found = " + artistFound);
			throw new ResourceAlreadyExistsException("Album already exists");
		}
		
		log.info("Saving album");
		Album albumToBeSaved = new Album();
		albumToBeSaved.setArtist(artistFound);
		albumToBeSaved.setName(dto.getName());
		albumToBeSaved.setReleaseDate(dto.getReleaseDate());
		Album albumSaved = this.albumsRepository.save(albumToBeSaved);
		log.info("Album Saved: " + albumSaved);
		ResponseAlbumDto response = new ResponseAlbumDto(albumSaved.getId(), albumSaved.getName(), albumSaved.getReleaseDate());
		return response;
	}

	@Override
	public ResponseAlbumDto getAlbum(Long artistId, Long albumId) {
		log.info("Finding artist by id = " + artistId);
		Artist artistFound = this.artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		log.info("Artist found" + artistFound);
		Album albumFound = null;
		
		log.info("Finding album of artist");
		if(artistFound.albums.size() > 0) {
			for(Album currentAlbum : artistFound.albums) {
				if(currentAlbum.getId().equals(albumId)) {
					albumFound = currentAlbum;
				}
			}
		}
		
		if(albumFound == null) {
			log.warning("Album not found");
			throw new ResourceNotFoundException("Album not found");
		}
		
		log.info("Albumn found = " + albumFound);
		ResponseAlbumDto dto = new ResponseAlbumDto(albumFound.getId(), albumFound.getName(), albumFound.getReleaseDate());
		return dto;
	}

	@Override
	public ResponseAlbumDto updateAlbum(Long artistId, Long albumId, CreateOrUpdateAlbumDto dto) {
		log.info("Finding artist by id = " + artistId);
		Artist artistFound = this.artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		Album albumFound = null;
		
		log.info("Finding album of artist");
		if(artistFound.albums.size() > 0) {
			for(Album currentAlbum : artistFound.albums) {
				if(currentAlbum.getId().equals(albumId)) {
					albumFound = currentAlbum;
				}
			}
		}
		
		if(albumFound == null) {
			log.warning("Album not found");
			throw new ResourceNotFoundException("Album not found");
		}
		
		log.info("Album found = " + albumFound);
		albumFound.setName(dto.getName());
		albumFound.setReleaseDate(dto.getReleaseDate());
		
		log.info("Updating album");
		Album albumSaved = this.albumsRepository.save(albumFound);
		log.info("Artist updated successfully = " + albumSaved);
		ResponseAlbumDto response = new ResponseAlbumDto(albumSaved.getId(), albumSaved.getName(), albumSaved.getReleaseDate());
		return response;
	}

	@Override
	public void deleteAlbum(Long artistId, Long albumId) {
		log.info("Finding artist by id = " + artistId);
		Artist artistFound = this.artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		Album albumFound = null;
		
		log.info("Finding album of artist");
		if(artistFound.albums.size() > 0) {
			for(Album currentAlbum : artistFound.albums) {
				if(currentAlbum.getId().equals(albumId)) {
					albumFound = currentAlbum;
				}
			}
		} 
		
		if(albumFound == null) {
			log.warning("Album not found");
			throw new ResourceNotFoundException("Album not found");
		}
		
		log.info("Album found = " + albumFound);
		log.info("Deleting album");
		this.albumsRepository.delete(albumFound);
		log.info("Artist deleted successfully");
	}

	@Override
	public List<ResponseAlbumDto> getAllAlbumsOfAnArtist(Long artistId) {
		log.info("Finding artist by id = " + artistId);
		Artist artistFound = this.artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		log.info("Artist found = " + artistFound);
		
		log.info("Finding albums of artist");
		List<Album> albums = artistFound.getAlbums();
		log.info("Albums of artist found: " + albums.size());
		List<ResponseAlbumDto> dtos = new ArrayList<>();
		albums.forEach(album -> {
			ResponseAlbumDto newDto = new ResponseAlbumDto(album.getId(), album.getName(), album.getReleaseDate());
			dtos.add(newDto);
		});
		return dtos;
	}

}
