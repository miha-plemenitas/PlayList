package com.playlist.rating_service.model;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import org.bson.types.ObjectId;

public class Rating extends ReactivePanacheMongoEntity {

    public ObjectId userId;
    public ObjectId gameId;
    public int rating; // Rating from 1 to 10

    // Empty constructor is needed by Quarkus / Panache
    public Rating() {}
}
