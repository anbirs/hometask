package com.example.hometask.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Showtime {

    private Long id;
    private Long movieId;
    private String theater;
    private Integer maxSeats;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
