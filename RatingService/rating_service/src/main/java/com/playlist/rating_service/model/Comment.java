package com.playlist.rating_service.model;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import org.bson.types.ObjectId;

public class Comment extends ReactivePanacheMongoEntity {

    public ObjectId userId;
    public ObjectId gameId;
    public String comment;
    public long createdAt; // Unix timestamp (System.currentTimeMillis())

    // Empty constructor is needed by Quarkus / Panache
    public Comment() {}
}
