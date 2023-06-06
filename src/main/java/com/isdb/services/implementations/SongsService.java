package com.isdb.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
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
	
	@Autowired
	private ArtistsRepository artistsRepository;
	
	@Autowired
	private SongsRepository songsRepository;

	@Override
	public ResponseSongDto createSong(Long artistId, Long albumId, CreateOrUpdateSongDto dto) {
		Artist artistFound = artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		Album albumFound = null;
		
		if(artistFound.albums.size() > 0) {
			for(Album album : artistFound.albums) {
				if(album.getId().equals(albumId)) {
					albumFound = album;
					break;
				}
			}
		}
		
		if(albumFound == null) {
			throw new ResourceNotFoundException("Album not found");
		}
		
		Song newSong = new Song(albumFound, dto.getName(), dto.getDuration());
		newSong = this.songsRepository.save(newSong);
		ResponseSongDto responseDto = new ResponseSongDto(newSong.getId(), newSong.getName(), newSong.getDuration());
		return responseDto;
	}

	@Override
	public ResponseSongDto getSong(Long artistId, Long albumId, Long id) {
		Artist artistFound = artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		Album albumFound = null;
		
		if(artistFound.albums.size() > 0) {
			for(Album album : artistFound.albums) {
				if(album.getId().equals(albumId)) {
					albumFound = album;
					break;
				}
			}
		}
		
		if(albumFound == null) {
			throw new ResourceNotFoundException("Album not found");
		}
		
		Song songFound = null;
		
		if(albumFound.songs.size() > 0) {
			for(Song song : albumFound.songs) {
				if(song.getId().equals(id)) {
					songFound = song;
					break;
				}
			}
		}
		
		if(songFound == null) {
			throw new ResourceNotFoundException("Song not found");
		}
		
		ResponseSongDto responseDto = new ResponseSongDto(songFound.getId(), songFound.getName(), songFound.getDuration());
		return responseDto;
	}

	@Override
	public ResponseSongDto updateSong(Long artistId, Long albumId, Long id, CreateOrUpdateSongDto dto) {
		Artist artistFound = artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		Album albumFound = null;
		
		if(artistFound.albums.size() > 0) {
			for(Album album : artistFound.albums) {
				if(album.getId().equals(albumId)) {
					albumFound = album;
					break;
				}
			}
		}
		
		if(albumFound == null) {
			throw new ResourceNotFoundException("Album not found");
		}
		
		Song songFound = null;
		
		if(albumFound.songs.size() > 0) {
			for(Song song : albumFound.songs) {
				if(song.getId().equals(id)) {
					songFound = song;
					break;
				}
			}
		}
		
		if(songFound == null) {
			throw new ResourceNotFoundException("Song not found");
		}
		
		songFound.setName(dto.getName());
		songFound.setDuration(dto.getDuration());
		
		Song songUpdated = this.songsRepository.save(songFound);
		
		ResponseSongDto responseDto = new ResponseSongDto(songUpdated.getId(), songUpdated.getName(), songUpdated.getDuration());
		return responseDto;
	}

	@Override
	public void deleteSong(Long artistId, Long albumId, Long id) {
		Artist artistFound = artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		Album albumFound = null;
		
		if(artistFound.albums.size() > 0) {
			for(Album album : artistFound.albums) {
				if(album.getId().equals(albumId)) {
					albumFound = album;
					break;
				}
			}
		}
		
		if(albumFound == null) {
			throw new ResourceNotFoundException("Album not found");
		}
		
		Song songFound = null;
		
		if(albumFound.songs.size() > 0) {
			for(Song song : albumFound.songs) {
				if(song.getId().equals(id)) {
					songFound = song;
					break;
				}
			}
		}
		
		if(songFound == null) {
			throw new ResourceNotFoundException("Song not found");
		}
		
		this.songsRepository.delete(songFound);
	}

	@Override
	public List<ResponseSongDto> getAllSongsOfAlbum(Long artistId, Long albumId) {
		Artist artistFound = artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
		Album albumFound = null;
		
		if(artistFound.albums.size() > 0) {
			for(Album album : artistFound.albums) {
				if(album.getId().equals(albumId)) {
					albumFound = album;
					break;
				}
			}
		}
		
		if(albumFound == null) {
			throw new ResourceNotFoundException("Album not found");
		}
		
		List<ResponseSongDto> songsOfAlbum = new ArrayList<>();
		
		albumFound.songs.forEach(song -> {
			ResponseSongDto dto = new ResponseSongDto(song.getId(), song.getName(), song.getDuration());
			songsOfAlbum.add(dto);
		});
		
		return songsOfAlbum;
	}
	
	

}
