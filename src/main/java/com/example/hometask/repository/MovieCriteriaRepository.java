package com.example.hometask.repository;

import com.example.hometask.service.MovieField;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MovieCriteriaRepository {
    List<Object[]> findAllMoviesByDynamicFields(Set<MovieField> fields);

}

