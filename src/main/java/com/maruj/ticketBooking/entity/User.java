package com.maruj.ticketBooking.entity;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class User {
    private String name;
    private String password;
    private String hashedPassword;
    private List<Ticket> ticketsBooked;
    private String userId;

    public User(){
        this.userId = UUID.randomUUID().toString();
    }

}
