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

import com.isdb.dtos.albums.CreateOrUpdateAlbumDto;
import com.isdb.dtos.albums.ResponseAlbumDto;
import com.isdb.dtos.artists.CreateOrUpdateArtistDto;
import com.isdb.dtos.artists.ResponseArtistDto;
import com.isdb.dtos.songs.CreateOrUpdateSongDto;
import com.isdb.dtos.songs.ResponseSongDto;
import com.isdb.services.interfaces.AlbumsServiceInterface;
import com.isdb.services.interfaces.ArtistsServiceInterface;
import com.isdb.services.interfaces.SongsServiceInterface;

@RestController
@RequestMapping("/api/v1/artists")
public class ArtistsController {

	@Autowired
	private ArtistsServiceInterface artistsService;

	@Autowired
	private AlbumsServiceInterface albumsService;
	
	@Autowired
	private SongsServiceInterface songsService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<ResponseArtistDto>> getAllGenres() {
		List<ResponseArtistDto> responseDto = this.artistsService.getAllArtists();
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ResponseArtistDto> createGenre(@RequestBody CreateOrUpdateArtistDto dto) {
		ResponseArtistDto responseDto = this.artistsService.createArtist(dto);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseArtistDto> getGenre(@PathVariable(value = "id") Long id) {
		ResponseArtistDto responseDto = this.artistsService.getArtist(id);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseArtistDto> updateGenre(@PathVariable(value = "id") Long id,
			@RequestBody CreateOrUpdateArtistDto dto) {
		ResponseArtistDto responseDto = this.artistsService.updateArtist(id, dto);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Void> deleteGenre(@PathVariable(value = "id") Long id) {
		this.artistsService.deleteArtist(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{artistId}/albums")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<ResponseAlbumDto>> getAllAlbums(@PathVariable("artistId") Long artistId) {
		List<ResponseAlbumDto> responseDto = this.albumsService.getAllAlbumsOfAnArtist(artistId);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@PostMapping("/{artistId}/albums")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ResponseAlbumDto> createAlbum(@PathVariable("artistId") Long artistId,
			@RequestBody CreateOrUpdateAlbumDto dto) {
		ResponseAlbumDto responseDto = this.albumsService.createAlbum(artistId, dto);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	@GetMapping("/{artistId}/albums/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseAlbumDto> getAlbum(@PathVariable(value = "artistId") Long artistId,
			@PathVariable(value = "id") Long albumId) {
		ResponseAlbumDto responseDto = this.albumsService.getAlbum(artistId, albumId);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@PutMapping("/{artistId}/albums/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseAlbumDto> updateAlbum(@PathVariable(value = "artistId") Long artistId,
			@PathVariable(value = "id") Long albumId, @RequestBody CreateOrUpdateAlbumDto dto) {
		ResponseAlbumDto responseDto = this.albumsService.updateAlbum(artistId, albumId, dto);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@DeleteMapping("/{artistId}/albums/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Void> deleteAlbum(@PathVariable(value = "artistId") Long artistId,
			@PathVariable(value = "artistId") Long albumId) {
		this.albumsService.deleteAlbum(artistId, albumId);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{artistId}/albums/{albumId}/songs")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<ResponseSongDto>> getAllSongs(@PathVariable("artistId") Long artistId, @PathVariable("albumId") Long albumId) {
		List<ResponseSongDto> responseDto = this.songsService.getAllSongsOfAlbum(artistId, albumId);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@PostMapping("/{artistId}/albums/{albumId}/songs")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ResponseSongDto> createSong(@PathVariable("artistId") Long artistId, @PathVariable("albumId") Long albumId, @RequestBody CreateOrUpdateSongDto dto) {
		ResponseSongDto responseDto = this.songsService.createSong(artistId, albumId, dto);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}
	
	@GetMapping("/{artistId}/albums/{albumId}/songs/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseSongDto> getSong(@PathVariable("artistId") Long artistId, @PathVariable("albumId") Long albumId, @PathVariable("id") Long id) {
		ResponseSongDto responseDto = this.songsService.getSong(artistId, albumId, id);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	@PutMapping("/{artistId}/albums/{albumId}/songs/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseSongDto> updateSong(
			@PathVariable("artistId") Long artistId,
			@PathVariable("albumId") Long albumId,
			@PathVariable("id") Long id,
			@RequestBody CreateOrUpdateSongDto dto) {
		ResponseSongDto responseDto = this.songsService.updateSong(artistId, albumId, id, dto);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/{artistId}/albums/{albumId}/songs/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseSongDto> deleteSong(@PathVariable("artistId") Long artistId, @PathVariable("albumId") Long albumId, @PathVariable("id") Long id) {
		this.songsService.deleteSong(artistId, albumId, id);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}
