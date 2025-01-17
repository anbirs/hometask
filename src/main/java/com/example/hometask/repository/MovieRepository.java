package com.example.hometask.repository;

import com.example.hometask.repository.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long>, MovieCriteriaRepository {}

