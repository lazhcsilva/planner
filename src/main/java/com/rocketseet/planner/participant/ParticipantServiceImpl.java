package com.rocketseet.planner.participant;

import com.rocketseet.planner.trip.Trip;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    public ParticipantServiceImpl(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite.stream()
                .map(email -> new Participant(email, trip))
                .toList();

        this.participantRepository.saveAll(participants);
        System.out.println(participants.get(0).getId());
    }

    @Override
    public void triggerConfirmationEmailToParticipants(UUID tripId) {

    }

    @Override
    public void triggerConfirmationEmailToParticipant(String email) {

    }

    @Override
    public ParticipantCreateResponse registerParticipantToTrip(String email, Trip trip) {
        Participant newParticipant = new Participant(email, trip);
        this.participantRepository.save(newParticipant);
        return new ParticipantCreateResponse(newParticipant.getId());
    }

    @Override
    public List<ParticipantData> getAllParticipantsFromTrip(UUID tripId) {
        return this.participantRepository.findByTripId(tripId).stream()
                .map(participant -> new ParticipantData(participant.getId(),
                        participant.getName(), participant.getEmail(), participant.getConfirmed()))
                .toList();
    }
}
