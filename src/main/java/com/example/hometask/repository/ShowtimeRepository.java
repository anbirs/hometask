package com.example.hometask.repository;

import com.example.hometask.repository.entity.ShowtimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<ShowtimeEntity, Long> {
    List<ShowtimeEntity> findByMovieIdAndTheaterIgnoreCase(Long movieId, String theaterName);
    List<ShowtimeEntity> findByMovieId(Long movieId);
    List<ShowtimeEntity> findByTheaterIgnoreCase(String theaterName);
}

