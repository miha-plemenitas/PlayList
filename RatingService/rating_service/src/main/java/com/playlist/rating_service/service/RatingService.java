package com.playlist.rating_service.service;

import com.playlist.rating_service.audit.AuditLog;
import com.playlist.rating_service.model.Rating;
import com.playlist.rating_service.repository.RatingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import io.smallrye.mutiny.Uni;

import java.util.List;

@ApplicationScoped
@AuditLog
public class RatingService {

    @Inject
    RatingRepository ratingRepository;

    public Uni<Rating> submitRating(Rating rating) {
        return ratingRepository.persist(rating);
    }

    public Uni<List<Rating>> getRatingsByGameId(ObjectId gameId) {
        return ratingRepository.find("gameId", gameId).list();
    }
}
