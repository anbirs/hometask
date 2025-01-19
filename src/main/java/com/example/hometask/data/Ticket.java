package com.example.hometask.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ticket {

    private Long id;
    private Long userId;
    private Long showtimeId;
    private String seatNumber;
    private BigDecimal price;

}
