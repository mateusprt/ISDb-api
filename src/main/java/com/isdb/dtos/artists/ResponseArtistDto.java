package com.isdb.dtos.artists;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.isdb.dtos.genre.ResponseGenreDto;

public class ResponseArtistDto {
	
	private Long id;
	private ResponseGenreDto genre;
	private String name;
	
	@JsonProperty("birth_date")
	private Date birthDate;
	private String description;
	
	
	public ResponseArtistDto() {
	}

	public ResponseArtistDto(Long id, ResponseGenreDto genre, String name, Date birthDate, String description) {
		this.id = id;
		this.genre = genre;
		this.name = name;
		this.birthDate = birthDate;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ResponseGenreDto getGenre() {
		return genre;
	}

	public void setGenre(ResponseGenreDto genre) {
		this.genre = genre;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
