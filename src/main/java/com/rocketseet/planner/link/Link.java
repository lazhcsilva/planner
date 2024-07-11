package com.rocketseet.planner.link;

import com.rocketseet.planner.trip.Trip;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "links")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Link() {
    }

    public Link(String title, String url, Trip trip) {
        this.title = title;
        this.url = url;
        this.trip = trip;
    }

    public Link(UUID id, String title, String url, Trip trip) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.trip = trip;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
