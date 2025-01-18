package com.example.hometask.repository;

import com.example.hometask.repository.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findByUser_Id(Long userId);
    List<TicketEntity> findByShowtime_Id(Long showtimeId);

}
