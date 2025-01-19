package com.example.hometask.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String error;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(null, data);
    }

    public static <T> ApiResponse<T> error(String errorMessage) {
        return new ApiResponse<>(errorMessage, null);
    }
}
