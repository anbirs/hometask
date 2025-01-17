package com.example.hometask.service.mapper;
import com.example.hometask.repository.entity.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface MovieFieldMapper {
    MovieFieldMapper INSTANCE = Mappers.getMapper(MovieFieldMapper.class);

    MovieEntity map(Map<String, Object> fieldsMap);

    default Long mapToLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        throw new IllegalArgumentException("Cannot map value to Long: " + value);
    }

    default Integer mapToInteger(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        throw new IllegalArgumentException("Cannot map value to Integer: " + value);
    }

    default String mapToString(Object value) {
        return value != null ? value.toString() : null;
    }
}