package com.playlist.rating_service.service;

import com.playlist.rating_service.model.Comment;
import com.playlist.rating_service.repository.CommentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import io.smallrye.mutiny.Uni;

import java.util.List;

@ApplicationScoped
public class CommentService {

    @Inject
    CommentRepository commentRepository;

    public Uni<Comment> submitComment(Comment comment) {
        return commentRepository.persist(comment);
    }

    public Uni<List<Comment>> getCommentsByGameId(ObjectId gameId) {
        return commentRepository.find("gameId", gameId).list();
    }

    public Uni<Boolean> deleteComment(ObjectId commentId, ObjectId userId) {
        return commentRepository.findById(commentId)
                .onItem().ifNotNull().transformToUni(comment -> {
                    if (comment.userId.equals(userId)) {
                        return commentRepository.deleteById(commentId).replaceWith(true);
                    } else {
                        return Uni.createFrom().item(false);
                    }
                });
    }
}
