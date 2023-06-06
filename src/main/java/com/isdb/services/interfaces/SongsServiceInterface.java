package com.isdb.services.interfaces;

import java.util.List;

import com.isdb.dtos.songs.CreateOrUpdateSongDto;
import com.isdb.dtos.songs.ResponseSongDto;

public interface SongsServiceInterface {
	
	public ResponseSongDto createSong(Long artistId, Long albumId, CreateOrUpdateSongDto dto);
	public ResponseSongDto getSong(Long artistId, Long albumId, Long id);
	public ResponseSongDto updateSong(Long artistId, Long albumId, Long id, CreateOrUpdateSongDto dto);
	public void deleteSong(Long artistId, Long albumId, Long songId);
	public List<ResponseSongDto> getAllSongsOfAlbum(Long artistId, Long albumId);
	
}
