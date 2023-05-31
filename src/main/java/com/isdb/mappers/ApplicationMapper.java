package com.isdb.mappers;

import org.modelmapper.ModelMapper;

public class ApplicationMapper {
	
	private static ModelMapper mapper = new ModelMapper();
	
	public static <S,D> D mapObject(S sourceObject, Class<D> destinationClass) {
		return mapper.map(sourceObject, destinationClass);
	}
	
}
