package com.playlist.rating_service.service;

import com.playlist.rating_service.model.Rating;
import com.playlist.rating_service.model.Comment;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class MessagingService {

    @Inject
    RatingService ratingService;

    @Inject
    CommentService commentService;

    // Listen to incoming ratings
    @Incoming("rating-in")
    @Blocking
    public Uni<Void> consumeRating(Rating rating) {
        return ratingService.submitRating(rating)
                .onItem().ignore().andContinueWithNull();
    }

    // Listen to incoming comments
    @Incoming("comment-in")
    @Blocking
    public Uni<Void> consumeComment(Comment comment) {
        return commentService.submitComment(comment)
                .onItem().ignore().andContinueWithNull();
    }
}
