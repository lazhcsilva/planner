package com.rocketseet.planner.participant;

import com.rocketseet.planner.trip.Trip;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Participant() {
    }

    public Participant(String email, Trip trip) {
        this.email = email;
        this.trip = trip;
        this.isConfirmed = false;
        this.name = "";
    }

    public Participant(UUID id, Boolean isConfirmed, String name, String email, Trip trip) {
        this.id = id;
        this.isConfirmed = isConfirmed;
        this.name = name;
        this.email = email;
        this.trip = trip;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
