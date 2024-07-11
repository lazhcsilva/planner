package com.rocketseet.planner.trip;

import com.rocketseet.planner.activity.ActivityRequestPayload;
import com.rocketseet.planner.activity.ActivityResponse;
import com.rocketseet.planner.activity.ActivityService;
import com.rocketseet.planner.handler.BusinessException;
import com.rocketseet.planner.link.LinkRequestPayload;
import com.rocketseet.planner.link.LinkResponse;
import com.rocketseet.planner.link.LinkService;
import com.rocketseet.planner.participant.ParticipantCreateResponse;
import com.rocketseet.planner.participant.ParticipantRequestPayload;
import com.rocketseet.planner.participant.ParticipantService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final ParticipantService participantService;
    private final ActivityService activityService;
    private final LinkService linkService;

    public TripServiceImpl(TripRepository tripRepository,
                           ParticipantService participantService,
                           ActivityService activityService,
                           LinkService linkService) {
        this.tripRepository = tripRepository;
        this.participantService = participantService;
        this.activityService = activityService;
        this.linkService = linkService;
    }

    @Override
    public TripData getTripById(UUID tripId) {
        Optional<Trip> tripBD = tripRepository.findById(tripId);

        if (tripBD.isEmpty()) {
            throw new BusinessException("Trip not found from ID: " + tripId);
        }

        return new TripData(tripBD.get().getId(),
                tripBD.get().getDestination(),
                tripBD.get().getStartsAt(),
                tripBD.get().getEndsAt(),
                tripBD.get().getConfirmed(),
                tripBD.get().getOwnerName(),
                tripBD.get().getOwnerEmail());
    }

    @Override
    public Trip confirmTripById(UUID id) {
        Optional<Trip> tripBD = tripRepository.findById(id);
        if (tripBD.isEmpty()) {
            throw new BusinessException("Trip not found from ID: " + id);
        }

        Trip rawTrip = tripBD.get();
        rawTrip.setIsConfirmed(true);
        this.tripRepository.save(rawTrip);
        this.participantService.triggerConfirmationEmailToParticipants(id);

        return rawTrip;
    }

    @Override
    public void createTrip(Trip newTrip, TripRequestPayload payload) {
        try {
            verifyDateFormat(newTrip);
            this.tripRepository.save(newTrip);
            this.participantService.registerParticipantsToEvent(payload.email_to_invite(), newTrip);
        } catch (BusinessException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public ParticipantCreateResponse inviteParticipant(UUID id, ParticipantRequestPayload payload) {
        Optional<Trip> tripBD = tripRepository.findById(id);
        if (tripBD.isEmpty()) {
            throw new BusinessException("Trip not found from ID: " + id);
        }
        Trip rawTrip = tripBD.get();

        ParticipantCreateResponse participantResponse = this.participantService
                .registerParticipantToTrip(payload.email(), rawTrip);

        if (rawTrip.getConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());

        return participantResponse;
    }

    @Override
    public ActivityResponse registerActivityByTripId(UUID id, ActivityRequestPayload payload) {
        Optional<Trip> tripBD = tripRepository.findById(id);
        if (tripBD.isEmpty()) {
            throw new BusinessException("Trip not found from ID: " + id);
        }

        verifyActivityDate(tripBD, payload);

        Trip rawTrip = tripBD.get();

        return this.activityService.registerActivity(payload, rawTrip);
    }

    @Override
    public LinkResponse registerLinkByTripId(UUID id, LinkRequestPayload payload) {
        Optional<Trip> tripBD = tripRepository.findById(id);
        if (tripBD.isEmpty()) {
            throw new BusinessException("Trip not found from ID: " + id);
        }

        Trip rawTrip = tripBD.get();

        return this.linkService.registerLink(payload, rawTrip);
    }

    @Override
    public Trip updateTrip(UUID id, TripRequestPayload payload) {
        Optional<Trip> tripBD = tripRepository.findById(id);
        if (tripBD.isEmpty()) {
            throw new BusinessException("Trip not found from ID: " + id);
        }

        Trip rowTrip = tripBD.get();
        rowTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
        rowTrip.setStartsAt(LocalDateTime.parse(payload.start_at(), DateTimeFormatter.ISO_DATE_TIME));
        rowTrip.setDestination(payload.destination());
        this.tripRepository.save(rowTrip);
        return rowTrip;
    }

    private void verifyDateFormat(Trip newTrip) {

        LocalDateTime dateInit = newTrip.getStartsAt();
        LocalDateTime dateEnd = newTrip.getEndsAt();
        LocalDateTime dateNow = LocalDateTime.now();

        if (dateInit.isBefore(dateNow)) {
            throw new BusinessException("Start date cannot be in the past");
        }

        if (dateEnd.isBefore(dateInit)) {
            throw new BusinessException("End date cannot be before start date");
        }

    }

    private void verifyActivityDate(Optional<Trip> tripBD, ActivityRequestPayload payload) {
        LocalDateTime dateInit = tripBD.get().getStartsAt();
        LocalDateTime dateEnd = tripBD.get().getEndsAt();
        LocalDateTime newDateActivity = LocalDateTime.parse(payload.occurs_at());

        if (newDateActivity.isBefore(dateInit)) {
            throw new BusinessException("Activity date cannot be before initial date");
        }

        if (newDateActivity.isBefore(dateEnd)) {
            throw new BusinessException("Activity date cannot be after date final");
        }


    }
}
