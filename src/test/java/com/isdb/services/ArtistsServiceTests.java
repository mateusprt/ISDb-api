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

import com.isdb.dtos.artists.CreateOrUpdateArtistDto;
import com.isdb.dtos.artists.ResponseArtistDto;
import com.isdb.dtos.genre.ResponseGenreDto;
import com.isdb.exceptions.ResourceAlreadyExistsException;
import com.isdb.exceptions.ResourceNotFoundException;
import com.isdb.models.Artist;
import com.isdb.models.Genre;
import com.isdb.repositories.ArtistsRepository;
import com.isdb.repositories.GenresRepository;
import com.isdb.services.implementations.ArtistsService;

import jakarta.validation.ConstraintDeclarationException;


@SpringBootTest
public class ArtistsServiceTests {
	
	private static final Long ID = 1L;
	private static final String GENRE_NAME = "SAMBA";
	private static final String ARTIST_NAME = "JOHN DOE";
	private static final Date ARTIST_BIRTH_DATE = new Date();
	private static final String ARTIST_DESCRIPTION = "Some description";
	
	@InjectMocks
	private ArtistsService artistsService;
	
	@Mock
	private ArtistsRepository artistsRepository;
	
	@Mock
	private GenresRepository genresRepository;
	
	private Genre genre;
	private Optional<Genre> optionalGenre;
	private Artist artist;
	private Optional<Artist> optionalArtist;
	private CreateOrUpdateArtistDto artistDto;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		genre = new Genre(ID, GENRE_NAME);		
		optionalGenre = Optional.of(genre);
		
		artistDto = new CreateOrUpdateArtistDto(genre.getId(), ARTIST_NAME, ARTIST_BIRTH_DATE, ARTIST_DESCRIPTION);
		artist = new Artist(genre, ARTIST_NAME, ARTIST_BIRTH_DATE, ARTIST_DESCRIPTION);
		optionalArtist = Optional.of(artist);
	}
	
	@Test
	@DisplayName("Should not create an artist with inexistent genre_id")
	public void shouldNotCreateAnArtistWithInexistentGenreId() {
		final Long INEXISTENT_GENRE_ID = 2L;
		artistDto.setGenreId(INEXISTENT_GENRE_ID);
		when(genresRepository.findById(anyLong())).thenThrow(new ResourceNotFoundException("Genre not found"));
		
		try {
			ResponseArtistDto response = artistsService.createArtist(artistDto);
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Genre not found", e.getMessage());
		}
	}

	@Test
	@DisplayName("Should not create an artist with empty name")
	public void shouldNotCreateAnArtistWithEmptyName() {
		artistDto.setName("");
		artist.setName("");
		when(genresRepository.findById(anyLong())).thenReturn(optionalGenre);
		when(artistsRepository.save(artist)).thenThrow(new ConstraintDeclarationException("Name can't be blank", null));
		
		try {
			ResponseArtistDto response = artistsService.createArtist(artistDto);
			fail();
		} catch(Exception e) {
			assertEquals(ConstraintDeclarationException.class, e.getClass());
			assertEquals("Name can't be blank", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should not an artist that already exists")
	public void shouldNotCreateAnArtistThatAlreadyExists() {
		artist.setId(ID);
		when(genresRepository.findById(anyLong())).thenReturn(optionalGenre);
		when(artistsRepository.findByName(anyString())).thenReturn(artist);
		
		try {
			ResponseArtistDto response = artistsService.createArtist(artistDto);
			fail();
		} catch(Exception e) {
			assertEquals(ResourceAlreadyExistsException.class, e.getClass());
			assertEquals("Artist already exists", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should create an artist")
	public void shouldCreateAnArtist() {
		when(genresRepository.findById(anyLong())).thenReturn(optionalGenre);
		when(artistsRepository.findByName(anyString())).thenReturn(null);
		when(artistsRepository.save(artist)).thenReturn(artist);
		
		ResponseArtistDto response = artistsService.createArtist(artistDto);
		
		assertNotNull(response);
		assertEquals(ResponseArtistDto.class, response.getClass());
		assertEquals(GENRE_NAME, response.getGenre().getName());
		assertEquals(ARTIST_NAME, response.getName());
		assertEquals(ARTIST_BIRTH_DATE, response.getBirthDate());
		assertEquals(ARTIST_DESCRIPTION, response.getDescription());
		
		verify(genresRepository).findById(anyLong());
		verify(artistsRepository).findByName(anyString());
		verify(artistsRepository).save(artist);
	}
	
	@Test
	@DisplayName("Should not get an artist with inexistent id")
	public void shouldNotGetAnArtistWithInexistentId() {
		final Long INEXISTENT_ID = 2L;
		when(artistsRepository.findById(anyLong())).thenThrow(new ResourceNotFoundException("Artist not found"));
		
		try {
			ResponseArtistDto response = artistsService.getArtist(INEXISTENT_ID);
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Artist not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should not update an artist with inexistent id")
	public void shouldNotUpdateAGenreWithInexistentId() {
		final Long INEXISTENT_ID = 2L;
		when(artistsRepository.findById(anyLong())).thenThrow(new ResourceNotFoundException("Artist not found"));
		
		try {
			ResponseArtistDto response = artistsService.updateArtist(INEXISTENT_ID, artistDto);
		} catch(Exception e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Artist not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should not update an artist with empty name")
	public void shouldNotUpdateAnArtistWithEmptyName() {
		artistDto.setName("");
		artist.setName("");
		genre.setId(ID);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		when(artistsRepository.save(artist)).thenThrow(new ConstraintDeclarationException("Name can't be blank", null));
		
		try {
			ResponseArtistDto response = artistsService.updateArtist(ID, artistDto);
			fail();
		} catch(Exception e) {
			assertEquals(ConstraintDeclarationException.class, e.getClass());
			assertEquals("Name can't be blank", e.getMessage());
			verify(artistsRepository).findById(anyLong());
			verify(artistsRepository).save(artist);
		}
	}
	
	@Test
	@DisplayName("Should update an artist")
	public void shouldUpdateAnArtist() {
		artist.setId(ID);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		when(artistsRepository.save(artist)).thenReturn(artist);
		
		ResponseArtistDto response = this.artistsService.updateArtist(ID, artistDto);
		
		assertNotNull(response);
		assertEquals(ResponseArtistDto.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(GENRE_NAME, response.getGenre().getName());
		assertEquals(ARTIST_NAME, response.getName());
		assertEquals(ARTIST_BIRTH_DATE, response.getBirthDate());
		assertEquals(ARTIST_DESCRIPTION, response.getDescription());
		verify(artistsRepository).findById(anyLong());
		verify(artistsRepository).save(artist);
	}
	
	@Test
	@DisplayName("Should not delete an artist with inexistent id")
	public void shouldNotDeleteAnArtistWithInexistentId() {
		final Long INEXISTENT_ID = 2L;
		when(artistsRepository.findById(anyLong())).thenThrow(new ResourceNotFoundException("Artist not found"));
		
		try {
			this.artistsService.deleteArtist(INEXISTENT_ID);
		} catch(ResourceNotFoundException e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Artist not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should delete an artist")
	public void shouldDeleteAGenre() {
		artist.setId(ID);
		when(artistsRepository.findById(anyLong())).thenReturn(optionalArtist);
		
		artistsService.deleteArtist(ID);
		verify(artistsRepository).delete(artist);
		verify(artistsRepository).findById(ID);
	    verify(artistsRepository, never()).existsById(ID);
	}
	
	@Test
	@DisplayName("Should list all genres that was created")
	public void shouldListAllGenresThatWasCreated() {
		artist.setId(ID);
		List<Artist> listOfArtists = List.of(artist);
		final int SIZE_EXPECTED = 1;
		when(artistsRepository.findAll()).thenReturn(listOfArtists);
		
		List<ResponseArtistDto> response = artistsService.getAllArtists();

		assertEquals(SIZE_EXPECTED, response.size());
		assertEquals(ResponseArtistDto.class, response.get(0).getClass());
		assertEquals(ID, response.get(0).getId());
		assertEquals(GENRE_NAME, response.get(0).getGenre().getName());
		assertEquals(ARTIST_NAME, response.get(0).getName());
		assertEquals(ARTIST_BIRTH_DATE, response.get(0).getBirthDate());
		assertEquals(ARTIST_DESCRIPTION, response.get(0).getDescription());
	}

}
