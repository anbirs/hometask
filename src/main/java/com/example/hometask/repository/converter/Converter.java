package com.example.hometask.repository.converter;

public interface Converter<T, S> {
    T toDto(S entity);
    S toEntity(T dto);
}
