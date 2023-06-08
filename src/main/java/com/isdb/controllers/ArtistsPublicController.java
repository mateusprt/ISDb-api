package com.isdb.controllers;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.isdb.dtos.albums.ResponseAlbumDto;
import com.isdb.dtos.artists.ResponseArtistDto;
import com.isdb.dtos.songs.ResponseSongDto;
import com.isdb.services.interfaces.AlbumsServiceInterface;
import com.isdb.services.interfaces.ArtistsServiceInterface;
import com.isdb.services.interfaces.SongsServiceInterface;

@RestController
@RequestMapping("/api/v1/public/artists")
public class ArtistsPublicController {
	
	private static final Logger logger = Logger.getLogger(ArtistsController.class.toString());

	@Autowired
	private ArtistsServiceInterface artistsService;
	
	@Autowired
	private AlbumsServiceInterface albumsService;
	
	@Autowired
	private SongsServiceInterface songsService;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<ResponseArtistDto>> getAllArtists() {
		logger.info("Get all artists");
		List<ResponseArtistDto> responseDto = this.artistsService.getAllArtists();
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseArtistDto> getArtist(@PathVariable(value = "id") Long id) {
		logger.info("Get an artist");
		ResponseArtistDto responseDto = this.artistsService.getArtist(id);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@GetMapping("/{artistId}/albums")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<ResponseAlbumDto>> getAllAlbums(@PathVariable("artistId") Long artistId) {
		logger.info("Get all albums with artist_id = " + artistId);
		List<ResponseAlbumDto> responseDto = this.albumsService.getAllAlbumsOfAnArtist(artistId);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@GetMapping("/{artistId}/albums/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseAlbumDto> getAlbum(@PathVariable(value = "artistId") Long artistId,
			@PathVariable(value = "id") Long albumId) {
		logger.info("Get an album with artist_id = " + artistId + " and album_id = " + albumId);
		ResponseAlbumDto responseDto = this.albumsService.getAlbum(artistId, albumId);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@GetMapping("/{artistId}/albums/{albumId}/songs")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<ResponseSongDto>> getAllSongs(@PathVariable("artistId") Long artistId, @PathVariable("albumId") Long albumId) {
		logger.info("Get all songs with artist_id = " + artistId + " and album_id = " + albumId);
		List<ResponseSongDto> responseDto = this.songsService.getAllSongsOfAlbum(artistId, albumId);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@GetMapping("/{artistId}/albums/{albumId}/songs/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseSongDto> getSong(@PathVariable("artistId") Long artistId, @PathVariable("albumId") Long albumId, @PathVariable("id") Long id) {
		logger.info("Get a song with artist_id = " + artistId + ", album_id = " + albumId + " and id = " + id);
		ResponseSongDto responseDto = this.songsService.getSong(artistId, albumId, id);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
}
