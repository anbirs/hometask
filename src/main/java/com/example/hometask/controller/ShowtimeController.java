package com.example.hometask.controller;

import com.example.hometask.data.ApiResponse;
import com.example.hometask.data.Showtime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/showtimes")
@Tag(name = "Showtimes", description = "Showtime Management")
public interface ShowtimeController {

    @GetMapping
    @Operation(summary = "Get Showtimes", description = "Get all available and past showtime. Optionally - get showtimes by movieId and/or by theatre")
    ResponseEntity<ApiResponse<List<Showtime>>> getShowtimes(@RequestParam(required = false) Long movieId,
                                                             @RequestParam(required = false) String theaterName);

    @GetMapping("/{id}")
    @Operation(summary = "Get showtime by Id", description = "Get specific showtime")
    ResponseEntity<ApiResponse<Showtime>> getShowtimeById(@PathVariable Long id);

    @PostMapping
    @Operation(summary = "Create new Showtime", description = "Save new showtime")
    ResponseEntity<ApiResponse<Showtime>> addShowtime(@RequestBody Showtime showtime);

    @PutMapping("/{id}")
    @Operation(summary = "Update existing Showtime", description = "Change details of the Showtime")
    ResponseEntity<ApiResponse<Showtime>> updateShowtime(@PathVariable Long id, @RequestBody Showtime updatedShowtime);


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Showtime", description = "Remove the Showtime from the storage")
    ResponseEntity<ApiResponse<Long>> deleteShowtime(@PathVariable Long id);
}
