package com.playlist.rating_service;

import com.playlist.rating_service.model.Rating;
import com.playlist.rating_service.model.Comment;
import com.playlist.rating_service.service.RatingService;
import com.playlist.rating_service.service.CommentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger; // <-- ADD THIS IMPORT

import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RatingResource {

    private static final Logger logger = Logger.getLogger(RatingResource.class); // <-- ADD THIS LINE

    @Inject
    RatingService ratingService;

    @Inject
    CommentService commentService;

    // --- Rating Endpoints ---

    @POST
    @Path("/ratings")
    public Uni<Rating> submitRating(Rating rating) {
        logger.infof("Received request to submit rating: %s", rating);
        return ratingService.submitRating(rating);
    }

    @GET
    @Path("/ratings/{gameId}")
    public Uni<List<Rating>> getRatingsByGameId(@PathParam("gameId") String gameId) {
        logger.infof("Fetching ratings for gameId: %s", gameId);
        return ratingService.getRatingsByGameId(new ObjectId(gameId));
    }

    // --- Comment Endpoints ---

    @POST
    @Path("/comments")
    public Uni<Comment> submitComment(Comment comment) {
        comment.createdAt = System.currentTimeMillis(); // set timestamp
        logger.infof("Received request to submit comment: %s", comment);
        return commentService.submitComment(comment);
    }

    @GET
    @Path("/comments/{gameId}")
    public Uni<List<Comment>> getCommentsByGameId(@PathParam("gameId") String gameId) {
        logger.infof("Fetching comments for gameId: %s", gameId);
        return commentService.getCommentsByGameId(new ObjectId(gameId));
    }

    @DELETE
    @Path("/comments/{commentId}")
    public Uni<Boolean> deleteComment(@PathParam("commentId") String commentId, @QueryParam("userId") String userId) {
        logger.infof("Received request to delete commentId: %s by userId: %s", commentId, userId);
        return commentService.deleteComment(new ObjectId(commentId), new ObjectId(userId));
    }
}
