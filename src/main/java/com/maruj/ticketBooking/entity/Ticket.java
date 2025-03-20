package com.maruj.ticketBooking.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Data
public class Ticket {
    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private String dateOfTravel;
    private Train train;

    public Ticket(){
        this.ticketId = String.valueOf(100_000 + new Random().nextInt(900_000));
    }
}
