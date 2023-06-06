package com.isdb.dtos.songs;

public class ResponseSongDto {
	
	private Long id;
	private String name;
	private Integer duration;
	
	public ResponseSongDto(Long id, String name, Integer duration) {
		this.id = id;
		this.name = name;
		this.duration = duration;
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
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	

}
