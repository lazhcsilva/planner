package com.rocketseet.planner.link;

import com.rocketseet.planner.trip.Trip;

import java.util.List;
import java.util.UUID;

public interface LinkService {

    LinkResponse registerLink(LinkRequestPayload payload, Trip trip);
    List<LinkData> getAllLinksFromTrip(UUID tripId);

}
