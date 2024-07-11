package com.rocketseet.planner.activity;

import com.rocketseet.planner.handler.BusinessException;
import com.rocketseet.planner.trip.Trip;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip) {
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);

        this.activityRepository.save(newActivity);

        return new ActivityResponse(newActivity.getId());
    }

    @Override
    public List<ActivityData> getAllActivitiesFromTrip(UUID tripId) {
        List<Activity> activityList = this.activityRepository.findByTripId(tripId);

        if (activityList.isEmpty()) {
            throw new BusinessException("No activities found for trip ID: " + tripId);
        }

        return this.activityRepository.findByTripId(tripId).stream()
                .map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt()))
                .toList();
    }
}
