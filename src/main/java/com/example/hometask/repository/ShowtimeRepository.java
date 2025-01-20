package com.example.hometask.repository;

import com.example.hometask.repository.entity.ShowtimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<ShowtimeEntity, Long> {
    List<ShowtimeEntity> findByMovieIdAndTheaterIgnoreCase(Long movieId, String theaterName);
    List<ShowtimeEntity> findByMovieId(Long movieId);
    List<ShowtimeEntity> findByTheaterIgnoreCase(String theaterName);
    @Modifying
    @Transactional
    @Query("UPDATE ShowtimeEntity s SET s.movie = NULL WHERE s.movie.id = :movieId")
    void cleanMovieReferences(@Param("movieId") Long movieId);
}

