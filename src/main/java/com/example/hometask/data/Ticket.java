package com.example.hometask.data;

import java.math.BigDecimal;

public class Ticket {

    private String seatNumber;
    private BigDecimal price;

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
}
