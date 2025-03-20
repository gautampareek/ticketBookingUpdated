package com.maruj.ticketBooking.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class Train {

    private String trainId;
    private String trainNo;
    @Setter(AccessLevel.NONE)
    private List<List<Integer>> seats;
    private Map<String, String> stationTimes;
    private List<String> stations;

    public Train(){
        fillSeats();
        this.trainId = UUID.randomUUID().toString();

    }

    @Override
    public String toString() {
        return "Train{" +
                "trainNo='" + trainNo + '\'' +
                '}';
    }

    public void fillSeats() {
        if (seats == null) {
            seats = new ArrayList<>();
        }
        for (int i = 0; i < 5; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                row.add(0);
            }
            seats.add(row);
        }
    }
}
