package com.rocketseet.planner.trip;

import com.rocketseet.planner.activity.ActivityData;
import com.rocketseet.planner.activity.ActivityRequestPayload;
import com.rocketseet.planner.activity.ActivityResponse;
import com.rocketseet.planner.activity.ActivityService;
import com.rocketseet.planner.link.LinkData;
import com.rocketseet.planner.link.LinkRequestPayload;
import com.rocketseet.planner.link.LinkResponse;
import com.rocketseet.planner.link.LinkService;
import com.rocketseet.planner.participant.ParticipantCreateResponse;
import com.rocketseet.planner.participant.ParticipantData;
import com.rocketseet.planner.participant.ParticipantRequestPayload;
import com.rocketseet.planner.participant.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final ParticipantService participantService;
    private final TripRepository tripRepository;
    private final ActivityService activityService;
    private final LinkService linkService;

    public TripController(ParticipantService participantService,
                          TripRepository tripRepository,
                          ActivityService activityService,
                          LinkService linkService) {
        this.participantService = participantService;
        this.tripRepository = tripRepository;
        this.activityService = activityService;
        this.linkService = linkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        Optional<Trip> trip = this.tripRepository.findById(id);
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantData> participantList = this.participantService.getAllParticipantsFromTrip(id);
        return ResponseEntity.ok(participantList);
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id) {
        List<ActivityData> activityDatatList = this.activityService.getAllActivitiesFromTrip(id);
        return ResponseEntity.ok(activityDatatList);
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id) {
        List<LinkData> activityDatatList = this.linkService.getAllLinksFromTrip(id);
        return ResponseEntity.ok(activityDatatList);
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);
            this.tripRepository.save(rawTrip);
            this.participantService.triggerConfirmationEmailToParticipants(id);
            return ResponseEntity.ok(rawTrip);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);

        this.tripRepository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.email_to_invite(), newTrip);

        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id,
                                                         @RequestBody ParticipantRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            ParticipantCreateResponse participantResponse = this.participantService
                    .registerParticipantToTrip(payload.email(), rawTrip);

            if (rawTrip.getConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());
            return ResponseEntity.ok(participantResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id,
                                                     @RequestBody ActivityRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);
            return ResponseEntity.ok(activityResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id,
                                                     @RequestBody LinkRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            LinkResponse activityResponse = this.linkService.registerLink(payload, rawTrip);
            return ResponseEntity.ok(activityResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rowTrip = trip.get();
            rowTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rowTrip.setStartsAt(LocalDateTime.parse(payload.start_at(), DateTimeFormatter.ISO_DATE_TIME));
            rowTrip.setDestination(payload.destination());
            this.tripRepository.save(rowTrip);
            return ResponseEntity.ok(rowTrip);
        }
        return ResponseEntity.notFound().build();
    }


}