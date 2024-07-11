package com.rocketseet.planner.handler;

import java.time.Instant;

public record ResponseError(Instant timestamp, String status, int statusCode, String error) {

    public ResponseError(String status, int statusCode, String error) {
        this(Instant.now(), status, statusCode, error);
    }

}
