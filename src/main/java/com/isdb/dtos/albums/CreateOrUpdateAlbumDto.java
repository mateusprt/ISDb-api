package com.isdb.dtos.albums;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateOrUpdateAlbumDto {
	

	private String name;
	
	@JsonProperty("release_date")
	private Date releaseDate;
	

	public CreateOrUpdateAlbumDto() {
	}

	public CreateOrUpdateAlbumDto(String name, Date releaseDate) {
		this.name = name;
		this.releaseDate = releaseDate;
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
