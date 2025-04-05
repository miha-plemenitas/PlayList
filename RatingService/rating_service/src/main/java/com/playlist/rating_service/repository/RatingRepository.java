package com.playlist.rating_service.repository;

import com.playlist.rating_service.model.Rating;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RatingRepository implements ReactivePanacheMongoRepository<Rating> {
    // You can add custom methods here if needed later
}
