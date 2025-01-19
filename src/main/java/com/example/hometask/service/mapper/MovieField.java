package com.example.hometask.service.mapper;

import lombok.Getter;

import java.util.*;

@Getter
public enum MovieField {
    TITLE("title"),
    GENRE("genre"),
    DURATION("duration"),
    RATING("rating"),
    YEAR("releaseYear");

    private final String fieldName;

    MovieField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static List<MovieField> parseFields(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<MovieField> result = new ArrayList<>();
        Arrays.stream(input.split(","))
                .map(String::trim)
                .forEach(value -> {
                    for (MovieField field : MovieField.values()) {
                        if (field.getFieldName().equalsIgnoreCase(value)) {
                            result.add(field);
                            break;
                        }
                    }
                });
        return result;
    }
}
