package com.isdb.dtos.songs;

public class CreateOrUpdateSongDto {
	
	private String name;
	private Integer duration;
	
	public CreateOrUpdateSongDto(String name, Integer duration) {
		this.name = name;
		this.duration = duration;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

}
