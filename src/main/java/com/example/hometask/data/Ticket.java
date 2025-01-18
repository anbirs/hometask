package com.example.hometask.data;

import java.math.BigDecimal;

public class Ticket {

    private Long id;
    private Long user;
    private Long showtime;
    private String seatNumber;
    private BigDecimal price;

    public Ticket() {
    }

    public Ticket(Long id, Long user, Long showtime, String seatNumber, BigDecimal price) {
        this.id = id;
        this.user = user;
        this.showtime = showtime;
        this.seatNumber = seatNumber;
        this.price = price;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getShowtime() {
        return showtime;
    }

    public void setShowtime(Long showtime) {
        this.showtime = showtime;
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

    public Ticket(String seatNumber, BigDecimal price) {
        this.seatNumber = seatNumber;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
