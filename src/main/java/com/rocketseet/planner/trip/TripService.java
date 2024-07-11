package com.rocketseet.planner.trip;

import com.rocketseet.planner.activity.ActivityRequestPayload;
import com.rocketseet.planner.activity.ActivityResponse;
import com.rocketseet.planner.link.LinkRequestPayload;
import com.rocketseet.planner.link.LinkResponse;
import com.rocketseet.planner.participant.ParticipantCreateResponse;
import com.rocketseet.planner.participant.ParticipantRequestPayload;

import java.util.Optional;
import java.util.UUID;

public interface TripService {

    TripData getTripById(UUID id);
    Trip confirmTripById(UUID id);
    void createTrip(Trip newTrip, TripRequestPayload payload);
    ParticipantCreateResponse inviteParticipant(UUID id, ParticipantRequestPayload payload);
    ActivityResponse registerActivityByTripId(UUID id, ActivityRequestPayload payload);
    LinkResponse registerLinkByTripId(UUID id, LinkRequestPayload payload);
    Trip updateTrip(UUID id, TripRequestPayload payload);
}
