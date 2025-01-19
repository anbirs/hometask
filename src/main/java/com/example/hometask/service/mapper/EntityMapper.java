package com.example.hometask.service.mapper;

public interface EntityMapper<T, S> {
    T toDto(S entity);
    S toEntity(T dto);
    void updateEntityFromDto(T dto, S entity);
}
