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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final ParticipantService participantService;
    private final TripService tripService;
    private final ActivityService activityService;
    private final LinkService linkService;

    public TripController(ParticipantService participantService,
                          TripService tripService,
                          ActivityService activityService,
                          LinkService linkService) {
        this.participantService = participantService;
        this.tripService = tripService;
        this.activityService = activityService;
        this.linkService = linkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripData> getTripDetails(@PathVariable UUID id) {
        TripData trip = this.tripService.getTripById(id);
        return ResponseEntity.ok(trip);
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
        Trip trip = this.tripService.confirmTripById(id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);
        this.tripService.createTrip(newTrip, payload);
        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id,
                                                         @RequestBody ParticipantRequestPayload payload) {
        ParticipantCreateResponse participantResponse = this.tripService.inviteParticipant(id, payload);

        return  ResponseEntity.ok(participantResponse);
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id,
                                                     @RequestBody ActivityRequestPayload payload) {
        ActivityResponse activityResponse = this.tripService.registerActivityByTripId(id, payload);

        return ResponseEntity.ok(activityResponse);
    }

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id,
                                                     @RequestBody LinkRequestPayload payload) {
        LinkResponse activityResponse = this.tripService.registerLinkByTripId(id, payload);

        return ResponseEntity.ok(activityResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
        Trip newTrip = this.tripService.updateTrip(id, payload);

        return ResponseEntity.ok(newTrip);
    }


}