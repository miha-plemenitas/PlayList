package com.playlist.rating_service.repository;

import com.playlist.rating_service.model.Comment;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommentRepository implements ReactivePanacheMongoRepository<Comment> {
    // You can add custom methods here if needed later
}
