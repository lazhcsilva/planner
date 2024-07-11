package com.rocketseet.planner.activity;

import com.rocketseet.planner.trip.Trip;

import java.util.List;
import java.util.UUID;

public interface ActivityService {

    ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip);
    List<ActivityData> getAllActivitiesFromTrip(UUID tripId);

}
