package com.example.hometask.controller;

import com.example.hometask.data.Showtime;
import com.example.hometask.service.ShowtimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/showtimes")
public class ShowtimeControllerImpl implements ShowtimeController {
    @Autowired
    private ShowtimesService showtimesService;

    @Override
    public ResponseEntity<List<Showtime>> getShowtimes(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) String theaterName) {
        List<Showtime> showtimes = showtimesService.getShowtimes(movieId, theaterName);
        return ResponseEntity.ok(showtimes);    }

    @Override
    public ResponseEntity<Showtime> getShowtimeById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Showtime> addShowtime(Showtime showtime) {
        return null;
    }

    @Override
    public ResponseEntity<Showtime> updateShowtime(Long id, Showtime updatedShowtime) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteShowtime(Long id) {
        return null;
    }
}
