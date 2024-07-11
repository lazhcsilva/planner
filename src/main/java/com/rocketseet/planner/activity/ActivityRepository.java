package com.rocketseet.planner.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    @Query("SELECT a FROM Activity a WHERE a.trip.id = :tripId")
    List<Activity> findByTripId(UUID tripId);
}
