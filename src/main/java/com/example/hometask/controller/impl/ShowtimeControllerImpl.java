package com.example.hometask.controller.impl;

import com.example.hometask.controller.ShowtimeController;
import com.example.hometask.data.ApiResponse;
import com.example.hometask.data.Showtime;
import com.example.hometask.service.ShowtimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ShowtimeControllerImpl implements ShowtimeController {

    @Autowired
    private ShowtimesService showtimesService;

    @Override
    public ResponseEntity<ApiResponse<List<Showtime>>> getShowtimes(Long movieId, String theaterName) {
        List<Showtime> showtimes = showtimesService.getShowtimes(movieId, theaterName);
        return ResponseEntity.ok(ApiResponse.success(showtimes));
    }

    @Override
    public ResponseEntity<ApiResponse<Showtime>> getShowtimeById(Long id) {
        return ResponseEntity.ok(ApiResponse.success(showtimesService.getShowtimeById(id)));
    }

    @Override
    public ResponseEntity<ApiResponse<Showtime>> addShowtime(Showtime showtime) {
        return ResponseEntity.ok(ApiResponse.success(showtimesService.saveShowtime(showtime)));
    }

    @Override
    public ResponseEntity<ApiResponse<Showtime>> updateShowtime(Long id, Showtime updatedShowtime) {
        return ResponseEntity.ok(ApiResponse.success(showtimesService.updateShowtime(id, updatedShowtime)));
    }

    @Override
    public ResponseEntity<ApiResponse<Long>> deleteShowtime(Long id) {
        return ResponseEntity.ok(ApiResponse.success(showtimesService.deleteShowtime(id)));
    }
}
