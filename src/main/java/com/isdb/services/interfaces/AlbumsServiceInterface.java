package com.isdb.services.interfaces;

import java.util.List;

import com.isdb.dtos.albums.CreateOrUpdateAlbumDto;
import com.isdb.dtos.albums.ResponseAlbumDto;

public interface AlbumsServiceInterface {
	
	public ResponseAlbumDto createAlbum(Long artistId, CreateOrUpdateAlbumDto dto);
	public ResponseAlbumDto getAlbum(Long artistId, Long id);
	public ResponseAlbumDto updateAlbum(Long artistId, Long albumId, CreateOrUpdateAlbumDto dto);
	public void deleteAlbum(Long artistId, Long albumId);
	public List<ResponseAlbumDto> getAllAlbumsOfAnArtist(Long artistId);
	
}
