package com.isdb.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.isdb.dtos.albums.CreateOrUpdateAlbumDto;
import com.isdb.dtos.albums.ResponseAlbumDto;
import com.isdb.dtos.artists.ResponseArtistDto;
import com.isdb.exceptions.ResourceAlreadyExistsException;
import com.isdb.exceptions.ResourceNotFoundException;
import com.isdb.models.Album;
import com.isdb.models.Artist;
import com.isdb.models.Genre;
import com.isdb.repositories.AlbumsRepository;
import com.isdb.repositories.ArtistsRepository;
import com.isdb.services.implementations.AlbumsService;

import jakarta.validation.ConstraintDeclarationException;


@SpringBootTest
public class AlbumsServiceTests {
	
	private static final Long ID = 1L;
	private static final String GENRE_NAME = "SAMBA";
	private static final String ARTIST_NAME = "JOHN DOE";
	private static final Date ARTIST_BIRTH_DATE = new Date();
	private static final String ARTIST_DESCRIPTION = "Some description";
	
	private static final String ALBUM_NAME = "Some album";
	private static final Date RELEASE_DATE = new Date();
	
	@InjectMocks
	private AlbumsService albumsService;
	
	@Mock
	private AlbumsRepository albumsRepository;

	@Mock
	private ArtistsRepository artistsRepository;
	
	private Genre genre;
	private Artist artist;
	private Optional<Artist> optionalArtist;
	private Album album;
	private CreateOrUpdateAlbumDto albumDto;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		genre = new Genre(ID, GENRE_NAME);		
		artist = new Artist(genre, ARTIST_NAME, ARTIST_BIRTH_DATE, ARTIST_DESCRIPTION);
		optionalArtist = Optional.of(artist);
		
		album = new Album(artist, ALBUM_NAME, RELEASE_DATE);
		albumDto = new CreateOrUpdateAlbumDto(ALBUM_NAME, RELEASE_DATE);
		
		optionalArtist = Optional.of(artist);
	}
	
	@Test
	@DisplayName("Should not create an album with inexistent artist_id")
	public void shouldNotCreateAnAlbumWithInexistentArtistId() {
		final Long INEXISTENT_ARTIST_ID = 2L;
		when(artistsRepository.findById(anyLong())).thenThrow(new ResourceNotFoundException("Artist not found"));
		
		try {
			ResponseAlbumDto response = albumsService.createAlbum(INEXISTENT_ARTIST_ID, albumDto);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Artist not found", e.getMessage());
		}
	}

	@Test
	@DisplayName("Should not create an album with empty name")
	public void shouldNotCreateAnAlbumWithEmptyName() {
		albumDto.setName("");
		album.setName("");
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		when(albumsRepository.save(album)).thenThrow(new ConstraintDeclarationException("Name can't be blank", null));
		
		try {
			ResponseAlbumDto response = albumsService.createAlbum(ID, albumDto);
			fail();
		} catch(Exception e) {
			assertEquals(ConstraintDeclarationException.class, e.getClass());
			assertEquals("Name can't be blank", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should not create an album that already exists")
	public void shouldNotCreateAnAlbumThatAlreadyExists() {
		album.setId(ID);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		when(albumsRepository.findByName(anyString())).thenReturn(album);
		
		try {
			ResponseAlbumDto response = albumsService.createAlbum(ID, albumDto);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceAlreadyExistsException.class, e.getClass());
			assertEquals("Album already exists", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should create an artist")
	public void shouldCreateAnArtist() {
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		when(albumsRepository.findByName(anyString())).thenReturn(null);
		when(albumsRepository.save(album)).thenReturn(album);
		
		ResponseAlbumDto response = albumsService.createAlbum(ID, albumDto);
		
		assertNotNull(response);
		assertEquals(ResponseAlbumDto.class, response.getClass());
		assertEquals(ALBUM_NAME, response.getName());
		assertEquals(RELEASE_DATE, response.getReleaseDate());
		
		verify(artistsRepository).findById(anyLong());
		verify(albumsRepository).findByName(anyString());
		verify(albumsRepository).save(album);
	}
	
	@Test
	@DisplayName("Should not update an album with inexistent artist_id")
	public void shouldNotUpdateAGenreWithInexistentArtistId() {
		final Long ARTIST_ID = ID;
		final Long ALBUM_ID = ID;
		final Long INEXISTENT_ARTIST_ID = 4L;
		
		artist.setId(ARTIST_ID);
		
		when(artistsRepository.findById(anyLong())).thenThrow(new ResourceNotFoundException("Artist not found"));
		
		try {
			ResponseAlbumDto response = albumsService.updateAlbum(INEXISTENT_ARTIST_ID, ALBUM_ID, albumDto);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Artist not found", e.getMessage());
		}
	}
	

	@Test
	@DisplayName("Should not get an album with inexistent album_id")
	public void shouldNotGetAnAlbumWithInexistentAlbumId() {
		final Long ARTIST_ID = ID;
		final Long ALBUM_ID = ID;
		final Long INEXISTENT_ALBUM_ID = 4L;
		
		artist.setId(ARTIST_ID);
		album.setId(ALBUM_ID);
		artist.setAlbums(List.of(album));
		optionalArtist = Optional.of(artist);
		
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		
		try {
			ResponseAlbumDto response = albumsService.getAlbum(ARTIST_ID, INEXISTENT_ALBUM_ID);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Album not found", e.getMessage());
		}
	}


	@Test
	@DisplayName("Should not update an album with empty name")
	public void shouldNotUpdateAnAlbumWithEmptyName() {
		final Long ARTIST_ID = ID;
		final Long ALBUM_ID = ID;
		
		albumDto.setName("");
		artist.setName("");
		artist.setId(ARTIST_ID);
		album.setId(ALBUM_ID);
		album.setName("");
		
		artist.setAlbums(List.of(album));
		optionalArtist = Optional.of(artist);
		
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		when(albumsRepository.save(album)).thenThrow(new ConstraintDeclarationException("Name can't be blank", null));
		
		try {
			ResponseAlbumDto response = albumsService.updateAlbum(ARTIST_ID, ALBUM_ID, albumDto);
			fail();
		} catch(Exception e) {
			assertEquals(ConstraintDeclarationException.class, e.getClass());
			assertEquals("Name can't be blank", e.getMessage());
			verify(artistsRepository).findById(anyLong());
			verify(albumsRepository).save(album);
		}
	}
	
	@Test
	@DisplayName("Should update an album")
	public void shouldUpdateAnAlbum() {
		final Long ARTIST_ID = ID;
		final Long ALBUM_ID = ID;
		
		artist.setId(ARTIST_ID);
		album.setId(ALBUM_ID);
		
		artist.setAlbums(List.of(album));
		optionalArtist = Optional.of(artist);
		
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		when(albumsRepository.save(album)).thenReturn(album);
		
		ResponseAlbumDto response = albumsService.updateAlbum(ARTIST_ID, ALBUM_ID, albumDto);
		
		assertNotNull(response);
		assertEquals(ResponseAlbumDto.class, response.getClass());
		assertEquals(albumDto.getName(), response.getName());
		assertEquals(albumDto.getReleaseDate(), response.getReleaseDate());
		
		verify(artistsRepository).findById(anyLong());
		verify(albumsRepository).save(album);
	}
	
	@Test
	@DisplayName("Should not delete an album with inexistent artist_id")
	public void shouldNotDeleteAAlbumWithInexistentArtistId() {
		final Long ARTIST_ID = ID;
		final Long ALBUM_ID = ID;
		final Long INEXISTENT_ALBUM_ID = 4L;
		
		artist.setId(ARTIST_ID);
		album.setId(ALBUM_ID);
		artist.setAlbums(List.of(album));
		optionalArtist = Optional.of(artist);
		
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		
		try {
			albumsService.deleteAlbum(ARTIST_ID, INEXISTENT_ALBUM_ID);
			fail(); 
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Album not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should not delete an album with inexistent album_id")
	public void shouldNotDeleteAnAlbumWithInexistentAlbumId() {
		final Long ARTIST_ID = ID;
		final Long ALBUM_ID = ID;
		final Long INEXISTENT_ALBUM_ID = 2L;
		
		artist.setId(ARTIST_ID);
		album.setId(ALBUM_ID);
		artist.setAlbums(List.of(album));
		optionalArtist = Optional.of(artist);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		
		try {
			this.albumsService.deleteAlbum(ALBUM_ID, INEXISTENT_ALBUM_ID);
			fail(); 
		} catch(ResourceNotFoundException e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Album not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should delete an album")
	public void shouldDeleteAlbum() {
		final Long ALBUM_ID = 1L;
		final Long ARTIST_ID = 1L;
		album.setId(ALBUM_ID);
		artist.setAlbums(List.of(album));
		optionalArtist = Optional.of(artist);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		
		albumsService.deleteAlbum(ALBUM_ID, ARTIST_ID);
		verify(albumsRepository).delete(album);
		verify(artistsRepository).findById(ALBUM_ID);
	    verify(albumsRepository, never()).existsById(ALBUM_ID);
	}
	
	@Test
	@DisplayName("Should list all albums of an artist")
	public void shouldListAllAlbumsOfAnArtist() {
		album.setId(ID);
		artist.setAlbums(List.of(album));
		optionalArtist = Optional.of(artist);
		final int SIZE_EXPECTED = 1;
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		
		List<ResponseAlbumDto> response = albumsService.getAllAlbumsOfAnArtist(ID);

		assertEquals(SIZE_EXPECTED, response.size());
		assertEquals(ResponseAlbumDto.class, response.get(0).getClass());
		assertEquals(ID, response.get(0).getId());
		assertEquals(ALBUM_NAME, response.get(0).getName());
		assertEquals(RELEASE_DATE, response.get(0).getReleaseDate());
	}

}
