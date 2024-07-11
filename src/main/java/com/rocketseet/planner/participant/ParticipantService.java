package com.rocketseet.planner.participant;

import com.rocketseet.planner.trip.Trip;

import java.util.List;
import java.util.UUID;

public interface ParticipantService {

    void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip);
    void triggerConfirmationEmailToParticipants(UUID tripId);
    void triggerConfirmationEmailToParticipant(String email);
    ParticipantCreateResponse registerParticipantToTrip(String email, Trip trip);
    List<ParticipantData> getAllParticipantsFromTrip(UUID id);

}
