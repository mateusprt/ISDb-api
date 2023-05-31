package com.isdb.dtos.genre;

public class CreateOrUpdateGenreDto {
	
	private String name;
	
	public CreateOrUpdateGenreDto() {
	}

	public CreateOrUpdateGenreDto(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
