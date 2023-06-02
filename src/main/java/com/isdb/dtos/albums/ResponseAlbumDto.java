package com.isdb.dtos.albums;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseAlbumDto {
	
	private Long id;
	private String name;
	@JsonProperty("release_date")
	private Date releaseDate;
	
	public ResponseAlbumDto() {
	}

	public ResponseAlbumDto(Long id, String name, Date releaseDate) {
		this.id = id;
		this.name = name;
		this.releaseDate = releaseDate;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	
	
}
