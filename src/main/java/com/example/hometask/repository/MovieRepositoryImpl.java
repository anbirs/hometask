package com.example.hometask.repository;

import com.example.hometask.repository.entity.MovieEntity;
import com.example.hometask.service.mapper.MovieField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MovieRepositoryImpl implements MovieCriteriaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> findAllMoviesByDynamicFields(List<MovieField> fields) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<MovieEntity> movie = query.from(MovieEntity.class);

        List<Selection<?>> selections = fields.stream()
                .map(field -> movie.get(field.getFieldName()))
                .map(selection -> (Selection<?>) selection)
                .collect(Collectors.toList());

        query.multiselect(selections);
        return entityManager.createQuery(query).getResultList();
    }
}
