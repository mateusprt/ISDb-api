package com.isdb.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.isdb.dtos.songs.CreateOrUpdateSongDto;
import com.isdb.dtos.songs.ResponseSongDto;
import com.isdb.exceptions.ResourceNotFoundException;
import com.isdb.models.Album;
import com.isdb.models.Artist;
import com.isdb.models.Genre;
import com.isdb.models.Song;
import com.isdb.repositories.AlbumsRepository;
import com.isdb.repositories.ArtistsRepository;
import com.isdb.repositories.SongsRepository;
import com.isdb.services.implementations.SongsService;

import jakarta.validation.ConstraintDeclarationException;

@SpringBootApplication
public class SongsServiceTests {
	
	private static final Long ARTIST_ID = 1L;
	private static final Long ALBUM_ID = 1L;
	private static final String ALBUM_NAME = "Some name";
	private static final Long SONG_ID = 1L;
	private static final String SONG_NAME = "Some song";
	private static final Integer SONG_DURATION = 360;
	
	@InjectMocks
	private SongsService songsService;
	
	private CreateOrUpdateSongDto songDto;
	
	private Artist artist;
	private Optional<Artist> optionalArtist;
	
	private Album album;
	private Optional<Album> optionalAlbum;
	
	
	private Song song;
	private Optional<Song> optionalSong;
	
	@Mock
	private ArtistsRepository artistsRepository;
	
	@Mock
	private AlbumsRepository albumsRepository;
	
	@Mock
	private SongsRepository songsRepository;
	
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		songDto = new CreateOrUpdateSongDto(SONG_NAME, SONG_DURATION);
		
		Genre genre = new Genre(1L, "SOME GENRE");
		artist = new Artist(genre, SONG_NAME, new Date(), "Some Description");
		artist.setId(ARTIST_ID);
		
		album = new Album(artist, ALBUM_NAME, new Date());
		album.setId(ALBUM_ID);
		
		song = new Song(album, SONG_NAME, SONG_DURATION);
		song.setId(SONG_ID);
		
		album.setSongs(List.of(song));
		artist.setAlbums(List.of(album));
		
		optionalAlbum = Optional.of(album);
		optionalArtist = Optional.of(artist);
		
	}
	
	@Test
	@DisplayName("should not create a song with inexistent artist_id")
	public void shouldNotCreateASongWithInexistentArtistId() {
		final Long INEXISTENT_ARTIST_ID = 2L;
		when(artistsRepository.findById(INEXISTENT_ARTIST_ID)).thenThrow(new ResourceNotFoundException("Artist not found"));
		
		try {
			ResponseSongDto response = songsService.createSong(INEXISTENT_ARTIST_ID, ALBUM_ID, songDto);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Artist not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should not create a song with inexistent album_id")
	public void shouldNotCreateASongWithInexistentAlbumId() {
		final Long INEXISTENT_ALBUM_ID = 2L;
		artist.setAlbums(new ArrayList<>());
		optionalArtist = Optional.of(artist);
		when(artistsRepository.findById(ARTIST_ID)).thenReturn(optionalArtist);
		
		try {
			ResponseSongDto response = songsService.createSong(ARTIST_ID, INEXISTENT_ALBUM_ID, songDto);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Album not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should not create a song with empty name")
	public void shouldNotCreateASongWithEmptyName() {
		final String EMPTY_SONG_NAME = "";
		song.setId(null);
		songDto.setName(EMPTY_SONG_NAME);
		song.setName(EMPTY_SONG_NAME);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		when(songsRepository.save(song)).thenThrow(new ConstraintDeclarationException("Name can't be blank", null));
		
		try {
			ResponseSongDto response = songsService.createSong(ARTIST_ID, ALBUM_ID, songDto);
			fail();
		} catch(ConstraintDeclarationException e) {
			assertEquals(ConstraintDeclarationException.class, e.getClass());
			assertEquals("Name can't be blank", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should create a song")
	public void shouldCreateASong() {
		final Long NEW_SONG_ID = 2L;
		song.setId(null);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		when(songsRepository.save(song)).thenReturn(song);
		
		ResponseSongDto response = songsService.createSong(ARTIST_ID, ALBUM_ID, songDto);
		
		assertNotNull(response);
		assertEquals(ResponseSongDto.class, response.getClass());
		assertEquals(songDto.getName(), response.getName());
		assertEquals(songDto.getDuration(), response.getDuration());
		verify(artistsRepository).findById(anyLong());
		verify(songsRepository).save(song);
	}
	
	@Test
	@DisplayName("should not get a song with inexistent artist_id")
	public void shouldNotGetASongWithInexistentArtistId() {
		final Long INEXISTENT_ARTIST_ID = 2L;
		when(artistsRepository.findById(anyLong())).thenThrow(new ResourceNotFoundException("Artist not found"));
		
		try {
			ResponseSongDto response = songsService.getSong(INEXISTENT_ARTIST_ID, ALBUM_ID, SONG_ID);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Artist not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should not get a song with inexistent album_id")
	public void shouldNotGetASongWithInexistentAlbumId() {
		final Long INEXISTENT_ALBUM_ID = 2L;
		artist.setAlbums(new ArrayList<>());
		optionalArtist = Optional.of(artist);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		
		try {
			ResponseSongDto response = songsService.getSong(ARTIST_ID, INEXISTENT_ALBUM_ID, SONG_ID);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Album not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should not get a song with inexistent id")
	public void shouldNotGetASongWithEmptyName() {
		final Long INEXISTENT_SONG_ID = 2L;
		optionalArtist = Optional.of(artist);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		
		try {
			ResponseSongDto response = songsService.getSong(ARTIST_ID, ALBUM_ID, INEXISTENT_SONG_ID);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Song not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should get a song")
	public void shouldGetASong() {
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		
		ResponseSongDto response = songsService.getSong(ARTIST_ID, ALBUM_ID, SONG_ID);
		
		assertNotNull(response);
		assertEquals(ResponseSongDto.class, response.getClass());
		assertEquals(SONG_ID, response.getId());
		assertEquals(songDto.getName(), response.getName());
		assertEquals(songDto.getDuration(), response.getDuration());
		verify(artistsRepository).findById(anyLong());
	}
	
	@Test
	@DisplayName("should not update a song with inexistent artist_id")
	public void shouldNotUpdateASongWithInexistentArtistId() {
		final Long INEXISTENT_ARTIST_ID = 2L;
		when(artistsRepository.findById(INEXISTENT_ARTIST_ID)).thenThrow(new ResourceNotFoundException("Artist not found"));
		
		try {
			ResponseSongDto response = songsService.updateSong(INEXISTENT_ARTIST_ID, ALBUM_ID, SONG_ID, songDto);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Artist not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should not update a song with inexistent album_id")
	public void shouldNotUpdateASongWithInexistentAlbumId() {
		final Long INEXISTENT_ALBUM_ID = 2L;
		artist.setAlbums(new ArrayList<>());
		optionalArtist = Optional.of(artist);
		when(artistsRepository.findById(ARTIST_ID)).thenReturn(optionalArtist);
		
		try {
			ResponseSongDto response = songsService.updateSong(ARTIST_ID, INEXISTENT_ALBUM_ID, SONG_ID, songDto);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Album not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should not update a song with inexistent id")
	public void shouldNotUpdateASongWithInexistentId() {
		final Long INEXISTENT_SONG_ID = 2L;
		optionalArtist = Optional.of(artist);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		
		try {
			ResponseSongDto response = songsService.updateSong(ARTIST_ID, ALBUM_ID, INEXISTENT_SONG_ID, songDto);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Song not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should not update a song with empty name")
	public void shouldNotUpdateASongWithEmptyName() {
		final String EMPTY_SONG_NAME = "";
		songDto.setName(EMPTY_SONG_NAME);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		when(songsRepository.save(song)).thenThrow(new ConstraintDeclarationException("Name can't be blank", null));
		
		try {
			ResponseSongDto response = songsService.updateSong(ARTIST_ID, ALBUM_ID, SONG_ID, songDto);
			fail();
		} catch(ConstraintDeclarationException e) {
			assertEquals(ConstraintDeclarationException.class, e.getClass());
			assertEquals("Name can't be blank", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should update a song")
	public void shouldUpdateASong() {
		final String SONG_NAME_UPDATED = "Name updated";
		songDto.setName(SONG_NAME_UPDATED);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		when(songsRepository.save(song)).thenReturn(song);
		
		ResponseSongDto response = songsService.updateSong(ARTIST_ID, ALBUM_ID, SONG_ID, songDto);
		
		assertNotNull(response);
		assertEquals(ResponseSongDto.class, response.getClass());
		assertEquals(SONG_ID, response.getId());
		assertEquals(SONG_NAME_UPDATED, response.getName());
		assertEquals(songDto.getDuration(), response.getDuration());
		verify(artistsRepository).findById(anyLong());
		verify(songsRepository).save(song);
		
	}
	
	@Test
	@DisplayName("should not delete a song with inexistent artist_id")
	public void shouldNotDeleteASongWithInexistentArtistId() {
		final Long INEXISTENT_ARTIST_ID = 2L;
		when(artistsRepository.findById(INEXISTENT_ARTIST_ID)).thenThrow(new ResourceNotFoundException("Artist not found"));
		
		try {
			songsService.deleteSong(INEXISTENT_ARTIST_ID, ALBUM_ID, SONG_ID);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Artist not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should not delete a song with inexistent album_id")
	public void shouldNotDeleteASongWithInexistentAlbumId() {
		final Long INEXISTENT_ALBUM_ID = 2L;
		artist.setAlbums(new ArrayList<>());
		optionalArtist = Optional.of(artist);
		when(artistsRepository.findById(ARTIST_ID)).thenReturn(optionalArtist);
		
		try {
			songsService.deleteSong(ARTIST_ID, INEXISTENT_ALBUM_ID, SONG_ID);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Album not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should not delete a song with inexistent id")
	public void shouldNotDeleteASongWithEmptyName() {
		final Long INEXISTENT_SONG_ID = 2L;
		optionalArtist = Optional.of(artist);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		
		try {
			songsService.deleteSong(ARTIST_ID, ALBUM_ID, INEXISTENT_SONG_ID);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Song not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should delete a song")
	public void shouldDeleteASong() {
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		when(songsRepository.save(song)).thenReturn(song);
		
		 songsService.deleteSong(ARTIST_ID, ALBUM_ID, SONG_ID);
		
		 verify(artistsRepository).findById(ALBUM_ID);
		 verify(songsRepository).delete(song);
		 verify(songsRepository, never()).existsById(SONG_ID);
	}
	
	@Test
	@DisplayName("should not list all songs of an album with inexistent artist_id")
	public void shouldNotListAllSongsOfAnAlbumWithInexistentArtistId() {
		final Long INEXISTENT_ARTIST_ID = 2L;
		when(artistsRepository.findById(anyLong())).thenThrow(new ResourceNotFoundException("Artist not found"));
		
		try {
			List<ResponseSongDto> response = songsService.getAllSongsOfAlbum(INEXISTENT_ARTIST_ID, ALBUM_ID);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Artist not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should not list all songs of an album with inexistent album_id")
	public void shouldNotListAllSongsOfAnAlbumWithInexistentAlbumId() {
		final Long INEXISTENT_ALBUM_ID = 2L;
		artist.setAlbums(new ArrayList<>());
		optionalArtist = Optional.of(artist);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		
		try {
			List<ResponseSongDto> response = songsService.getAllSongsOfAlbum(ARTIST_ID, INEXISTENT_ALBUM_ID);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Album not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("should list all songs of an album")
	public void shouldListAllSongsOfAnAlbum() {
		final int SIZE_EXPECTED = 1;
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);

		List<ResponseSongDto> response = songsService.getAllSongsOfAlbum(ARTIST_ID, ALBUM_ID);
		
		assertNotNull(response);
		assertEquals(SIZE_EXPECTED, response.size());
		assertEquals(ResponseSongDto.class, response.get(0).getClass());
		assertEquals(SONG_ID, response.get(0).getId());
		assertEquals(SONG_NAME, response.get(0).getName());
		assertEquals(SONG_DURATION, response.get(0).getDuration());
	}
	
}
