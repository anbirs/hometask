package com.example.hometask.repository.entity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ShowtimeEntity showtime;

    @ManyToOne
    private UserEntity user;

    private String seatNumber;
    private BigDecimal price;

    public TicketEntity(Long id, ShowtimeEntity showtime, UserEntity user, String seatNumber, BigDecimal price) {
        this.id = id;
        this.showtime = showtime;
        this.user = user;
        this.seatNumber = seatNumber;
        this.price = price;
    }

    public TicketEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ShowtimeEntity getShowtime() {
        return showtime;
    }

    public void setShowtime(ShowtimeEntity showtime) {
        this.showtime = showtime;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
