package com.example.hometask.repository;

import com.example.hometask.service.mapper.MovieField;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieCriteriaRepository {
    List<Object[]> findAllMoviesByDynamicFields(List<MovieField> fields);

}

