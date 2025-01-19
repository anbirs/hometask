package com.example.hometask.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie {

    private String title;
    private String genre;
    private Integer duration;
    private String rating;
    private Integer releaseYear;
    private Long id;

}
