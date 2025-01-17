package com.example.hometask.service.converter;

public interface Converter<T, S> {
    T toDto(S entity);
    S toEntity(T dto);
}
