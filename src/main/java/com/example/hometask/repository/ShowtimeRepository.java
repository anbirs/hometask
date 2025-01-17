package com.example.hometask.repository;

import com.example.hometask.repository.entity.ShowtimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends JpaRepository<ShowtimeEntity, Long> {}

