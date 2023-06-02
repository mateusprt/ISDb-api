package com.isdb.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.isdb.dtos.genre.CreateOrUpdateGenreDto;
import com.isdb.dtos.genre.ResponseGenreDto;
import com.isdb.exceptions.ResourceAlreadyExistsException;
import com.isdb.exceptions.ResourceNotFoundException;
import com.isdb.models.Genre;
import com.isdb.repositories.GenresRepository;
import com.isdb.services.implementations.GenresService;

import jakarta.validation.ConstraintDeclarationException;


@SpringBootTest
public class GenresServiceTests {
	
	private static final Long ID = 1L;
	private static final String NAME = "JOHN DOE";
	
	@InjectMocks
	private GenresService genresService;
	
	@Mock
	private GenresRepository genresRepository;
	
	private CreateOrUpdateGenreDto genreDto;
	private Genre genre;
	private Optional<Genre> optionalGenre;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		genreDto = new CreateOrUpdateGenreDto(NAME);
		optionalGenre = Optional.of(new Genre(ID, NAME));
		
		genre = new Genre();
		genre.setName(NAME);
	} 

	@Test
	@DisplayName("Should not create a genre with empty name")
	public void shouldNotCreateAGenreWithEmptyName() {
		genreDto.setName("");
		genre.setName("");
		when(genresRepository.findByName(anyString())).thenReturn(null);
		when(genresRepository.save(genre)).thenThrow(new ConstraintDeclarationException("Name can't be blank", null));
		
		try {
			ResponseGenreDto response = this.genresService.createGenre(genreDto);
			fail();
		} catch(ConstraintDeclarationException e) {
			assertEquals(ConstraintDeclarationException.class, e.getClass());
			assertEquals("Name can't be blank", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should not create a genre that already exists")
	public void shouldNotCreateAGenreThatAlreadyExists() {
		when(genresRepository.findByName(anyString())).thenReturn(genre);
		
		try {
			ResponseGenreDto response = this.genresService.createGenre(genreDto);
			fail();
		} catch(ResourceAlreadyExistsException e) {
			assertEquals(ResourceAlreadyExistsException.class, e.getClass());
			assertEquals("Genre already exists", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should create a genre")
	public void shouldCreateAGenre() {
		when(genresRepository.findByName(anyString())).thenReturn(null);
		when(genresRepository.save(genre)).thenReturn(genre);
		
		ResponseGenreDto response = this.genresService.createGenre(genreDto);
		
		assertNotNull(response);
		assertEquals(ResponseGenreDto.class, response.getClass());
		assertEquals(NAME, response.getName());
		
		verify(genresRepository).findByName(anyString());
		verify(genresRepository).save(genre);
	}
	
	@Test
	@DisplayName("Should not get a genre with inexistent id")
	public void shouldNotGetAGenreWithInexistentId() {
		when(genresRepository.findById(anyLong())).thenThrow(new ResourceNotFoundException("Genre not found"));
		
		try {
			ResponseGenreDto response = this.genresService.getGenre(ID);
		} catch(ResourceNotFoundException e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Genre not found", e.getMessage());
		}
	} 
	
	@Test
	@DisplayName("Should get a genre with existent id")
	public void shouldGetAGenreWithEnexistentId() {
		when(genresRepository.findById(anyLong())).thenReturn(optionalGenre);
		
		ResponseGenreDto response = this.genresService.getGenre(ID);
		
		assertNotNull(response);
		assertEquals(ResponseGenreDto.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NAME, response.getName());
	}
	
	@Test
	@DisplayName("Should not update a genre with inexistent id")
	public void shouldNotUpdateAGenreWithInexistentId() {
		when(genresRepository.findById(anyLong())).thenThrow(new ResourceNotFoundException("Genre not found"));
		
		try {
			ResponseGenreDto response = this.genresService.updateGenre(ID, genreDto);
		} catch(ResourceNotFoundException e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Genre not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should not update a genre with empty name")
	public void shouldNotUpdateAGenreWithEmptyName() {
		genreDto.setName("");
		genre.setName("");
		genre.setId(ID);
		when(genresRepository.findById(anyLong())).thenReturn(optionalGenre);
		when(genresRepository.save(genre)).thenThrow(new ConstraintDeclarationException("Name can't be blank", null));
		
		try {
			ResponseGenreDto response = this.genresService.updateGenre(ID, genreDto);
			fail();
		} catch(Exception e) {
			assertEquals(ConstraintDeclarationException.class, e.getClass());
			assertEquals("Name can't be blank", e.getMessage());
			verify(genresRepository).findById(anyLong());
			verify(genresRepository).save(genre);
		}
	}
	
	@Test
	@DisplayName("Should update a genre")
	public void shouldUpdateAGenre() {
		genre.setId(ID);
		when(genresRepository.findById(anyLong())).thenReturn(optionalGenre);
		when(genresRepository.save(genre)).thenReturn(genre);
		
		ResponseGenreDto response = this.genresService.updateGenre(ID, genreDto);
		
		assertNotNull(response);
		assertEquals(ResponseGenreDto.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NAME, response.getName());
		verify(genresRepository).findById(anyLong());
		verify(genresRepository).save(genre);
	}
	
	@Test
	@DisplayName("Should not delete a genre with inexistent id")
	public void shouldNoDeleteAGenreWithInexistentId() {
		when(genresRepository.findById(anyLong())).thenThrow(new ResourceNotFoundException("Genre not found"));
		
		try {
			this.genresService.deleteGenre(ID);
		} catch(ResourceNotFoundException e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
			assertEquals("Genre not found", e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should delete a genre")
	public void shouldDeleteAGenre() {
		genre.setId(ID);
		when(genresRepository.findById(anyLong())).thenReturn(optionalGenre);

		this.genresService.deleteGenre(ID);
		verify(genresRepository).delete(genre);
		verify(genresRepository).findById(ID);
	    verify(genresRepository, never()).existsById(ID);
	}
	
	@Test
	@DisplayName("Should list all genres that was created")
	public void shouldListAllGenresThatWasCreated() {
		genre.setId(ID);
		List<Genre> listOfGenres = List.of(genre);
		final int SIZE_EXPECTED = 1;
		when(genresRepository.findAll()).thenReturn(listOfGenres);
		
		List<ResponseGenreDto> response = genresService.getAllGenres();

		assertEquals(SIZE_EXPECTED, response.size());
		assertEquals(ResponseGenreDto.class, response.get(0).getClass());
		assertEquals(ID, response.get(0).getId());
		assertEquals(NAME, response.get(0).getName());
	}

}
