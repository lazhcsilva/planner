package com.rocketseet.planner.trip;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String destination;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;

    public Trip() {
    }

    public Trip(TripRequestPayload data) {
        this.destination = data.destination();
        this.isConfirmed = false;
        this.ownerEmail = data.owner_email();
        this.ownerName = data.owner_name();
        this.startsAt = LocalDateTime.parse(data.start_at(), DateTimeFormatter.ISO_DATE_TIME);
        this.endsAt = LocalDateTime.parse(data.ends_at(), DateTimeFormatter.ISO_DATE_TIME);
    }

    public Trip(UUID id,
                String destination,
                LocalDateTime startsAt,
                LocalDateTime endsAt,
                Boolean isConfirmed,
                String ownerName,
                String ownerEmail) {
        this.id = id;
        this.destination = destination;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.isConfirmed = isConfirmed;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
    }

    public UUID getId() {
        return id;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public void setIsConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
}
