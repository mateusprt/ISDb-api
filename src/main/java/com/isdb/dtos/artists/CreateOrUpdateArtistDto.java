package com.isdb.dtos.artists;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateOrUpdateArtistDto {
	
	@JsonProperty("genre_id")
	private Long genreId;
	private String name;
	
	@JsonProperty("birth_date")
	private Date birthDate;
	private String description;
	
	
	public CreateOrUpdateArtistDto() {
	}

	public CreateOrUpdateArtistDto(Long genreId, String name, Date birthDate, String description) {
		this.genreId = genreId;
		this.name = name;
		this.birthDate = birthDate;
		this.description = description;
	}

	public Long getGenreId() {
		return genreId;
	}

	public void setGenreId(Long genreId) {
		this.genreId = genreId;
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
