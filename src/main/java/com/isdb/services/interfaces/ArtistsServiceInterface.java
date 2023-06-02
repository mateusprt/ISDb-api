package com.isdb.services.interfaces;

import java.util.List;

import com.isdb.dtos.artists.CreateOrUpdateArtistDto;
import com.isdb.dtos.artists.ResponseArtistDto;

public interface ArtistsServiceInterface {
	
	public ResponseArtistDto createArtist(CreateOrUpdateArtistDto dto);
	public ResponseArtistDto getArtist(Long id);
	public ResponseArtistDto updateArtist(Long id, CreateOrUpdateArtistDto dto);
	public void deleteArtist(Long id);
	public List<ResponseArtistDto> getAllArtists();
	
}
