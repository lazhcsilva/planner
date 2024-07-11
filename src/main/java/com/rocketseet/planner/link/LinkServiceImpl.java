package com.rocketseet.planner.link;

import com.rocketseet.planner.activity.ActivityData;
import com.rocketseet.planner.activity.ActivityResponse;
import com.rocketseet.planner.trip.Trip;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    public LinkServiceImpl(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public LinkResponse registerLink(LinkRequestPayload payload, Trip trip) {
        Link newLink = new Link(payload.title(), payload.url(), trip);

        this.linkRepository.save(newLink);

        return new LinkResponse(newLink.getId());
    }

    @Override
    public List<LinkData> getAllLinksFromTrip(UUID tripId) {
        return this.linkRepository.findByTripId(tripId).stream()
                .map(link -> new LinkData(link.getTitle(), link.getUrl()))
                .toList();
    }
}
