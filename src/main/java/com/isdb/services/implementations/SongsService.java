package com.isdb.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isdb.dtos.songs.CreateOrUpdateSongDto;
import com.isdb.dtos.songs.ResponseSongDto;
import com.isdb.exceptions.ResourceNotFoundException;
import com.isdb.models.Album;
import com.isdb.models.Artist;
import com.isdb.models.Song;
import com.isdb.repositories.ArtistsRepository;
import com.isdb.repositories.SongsRepository;
import com.isdb.services.interfaces.SongsServiceInterface;

@Service
public class SongsService implements SongsServiceInterface {
	
	private static Logger log = Logger.getLogger(SongsService.class.getName());
	
	@Autowired
	private ArtistsRepository artistsRepository;
	
	@Autowired
	private SongsRepository songsRepository;

	@Override
	public ResponseSongDto createSong(Long artistId, Long albumId, CreateOrUpdateSongDto dto) {
		log.info("Finding artist by id = " + artistId);
		Artist artistFound = artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		log.info("Artist found" + artistFound);
		
		Album albumFound = null;
		
		log.info("Finding album of artist");
		if(artistFound.albums.size() > 0) {
			for(Album album : artistFound.albums) {
				if(album.getId().equals(albumId)) {
					albumFound = album;
					break;
				}
			}
		}
		
		if(albumFound == null) {
			log.warning("Album not found");
			throw new ResourceNotFoundException("Album not found");
		}
		
		log.info("Albumn found = " + albumFound);
		log.info("Saving song");
		Song newSong = new Song(albumFound, dto.getName(), dto.getDuration());
		newSong = this.songsRepository.save(newSong);
		log.info("Song Saved: " + newSong);
		ResponseSongDto responseDto = new ResponseSongDto(newSong.getId(), newSong.getName(), newSong.getDuration());
		return responseDto;
	}

	@Override
	public ResponseSongDto getSong(Long artistId, Long albumId, Long id) {
		log.info("Finding artist by id = " + artistId);
		Artist artistFound = artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		log.info("Artist found" + artistFound);
		Album albumFound = null;
		
		log.info("Finding album of artist");
		if(artistFound.albums.size() > 0) {
			for(Album album : artistFound.albums) {
				if(album.getId().equals(albumId)) {
					albumFound = album;
					break;
				}
			}
		}
		
		if(albumFound == null) {
			log.warning("Album not found");
			throw new ResourceNotFoundException("Album not found");
		}
		
		log.info("Albumn found = " + albumFound);
		
		Song songFound = null;
		
		log.info("Finding songs of album");
		if(albumFound.songs.size() > 0) {
			for(Song song : albumFound.songs) {
				if(song.getId().equals(id)) {
					songFound = song;
					break;
				}
			}
		}
		
		if(songFound == null) {
			log.warning("Songs not found");
			throw new ResourceNotFoundException("Song not found");
		}
		
		log.info("Song found = " + songFound);
		ResponseSongDto responseDto = new ResponseSongDto(songFound.getId(), songFound.getName(), songFound.getDuration());
		return responseDto;
	}

	@Override
	public ResponseSongDto updateSong(Long artistId, Long albumId, Long id, CreateOrUpdateSongDto dto) {
		log.info("Finding artist by id = " + artistId);
		Artist artistFound = artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		log.info("Artist found" + artistFound);
		Album albumFound = null;
		
		log.info("Finding album of artist");
		if(artistFound.albums.size() > 0) {
			for(Album album : artistFound.albums) {
				if(album.getId().equals(albumId)) {
					albumFound = album;
					break;
				}
			}
		}
		
		if(albumFound == null) {
			log.warning("Album not found");
			throw new ResourceNotFoundException("Album not found");
		}
		
		log.info("Albumn found = " + albumFound);
		
		Song songFound = null;
		
		log.info("Finding songs of album");
		if(albumFound.songs.size() > 0) {
			for(Song song : albumFound.songs) {
				if(song.getId().equals(id)) {
					songFound = song;
					break;
				}
			}
		}
		
		if(songFound == null) {
			log.warning("Songs not found");
			throw new ResourceNotFoundException("Song not found");
		}
		
		log.info("Song found = " + songFound);
		songFound.setName(dto.getName());
		songFound.setDuration(dto.getDuration());
		
		log.info("Updating song");
		Song songUpdated = this.songsRepository.save(songFound);
		log.info("Song updated successfully = " + songUpdated);
		ResponseSongDto responseDto = new ResponseSongDto(songUpdated.getId(), songUpdated.getName(), songUpdated.getDuration());
		return responseDto;
	}

	@Override
	public void deleteSong(Long artistId, Long albumId, Long id) {
		log.info("Finding artist by id = " + artistId);
		Artist artistFound = artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		log.info("Artist found" + artistFound);
		Album albumFound = null;
		
		log.info("Finding album of artist");
		if(artistFound.albums.size() > 0) {
			for(Album album : artistFound.albums) {
				if(album.getId().equals(albumId)) {
					albumFound = album;
					break;
				}
			}
		}
		
		if(albumFound == null) {
			log.warning("Album not found");
			throw new ResourceNotFoundException("Album not found");
		}
		
		log.info("Albumn found = " + albumFound);
		
		Song songFound = null;
		
		log.info("Finding songs of album");
		if(albumFound.songs.size() > 0) {
			for(Song song : albumFound.songs) {
				if(song.getId().equals(id)) {
					songFound = song;
					break;
				}
			}
		}
		
		if(songFound == null) {
			log.warning("Songs not found");
			throw new ResourceNotFoundException("Song not found");
		}
		
		log.info("Updating song");
		this.songsRepository.delete(songFound);
		log.info("Song deleted successfully");
	}

	@Override
	public List<ResponseSongDto> getAllSongsOfAlbum(Long artistId, Long albumId) {
		log.info("Finding artist by id = " + artistId);
		Artist artistFound = artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		log.info("Artist found" + artistFound);
		Album albumFound = null;
		
		log.info("Finding album of artist");
		if(artistFound.albums.size() > 0) {
			for(Album album : artistFound.albums) {
				if(album.getId().equals(albumId)) {
					albumFound = album;
					break;
				}
			}
		}
		
		if(albumFound == null) {
			log.warning("Album not found");
			throw new ResourceNotFoundException("Album not found");
		}
		
		log.info("Albumn found = " + albumFound);
		
		List<ResponseSongDto> songsOfAlbum = new ArrayList<>();
		
		log.info("Finding songs of album");
		albumFound.songs.forEach(song -> {
			ResponseSongDto dto = new ResponseSongDto(song.getId(), song.getName(), song.getDuration());
			songsOfAlbum.add(dto);
		});
		
		log.info("Songs found: " + albumFound.songs.size());
		
		return songsOfAlbum;
	}
	
	

}
