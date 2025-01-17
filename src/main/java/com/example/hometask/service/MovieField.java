package com.example.hometask.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum MovieField {
    TITLE("title"),
    GENRE("genre"),
    DURATION("duration"),
    RATING("rating"),
    YEAR("year");

    private final String fieldName;

    MovieField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<MovieField> parseFields(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new HashSet<>();
        }

        Set<MovieField> result = new HashSet<>();
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
