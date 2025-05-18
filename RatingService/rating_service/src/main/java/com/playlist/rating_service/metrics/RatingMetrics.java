package com.playlist.rating_service.metrics;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoDatabase;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.jboss.logging.Logger;

import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
@Startup
public class RatingMetrics {

    private static final Logger LOGGER = Logger.getLogger(RatingMetrics.class);

    @Inject
    ReactiveMongoClient mongoClient;

    @Inject
    MeterRegistry registry;

    private final AtomicLong ratingCount = new AtomicLong(0);

    @PostConstruct
    void init() {
        // Register gauge
        Gauge.builder("ratings_total_in_db", ratingCount, AtomicLong::get)
            .description("Current number of ratings in the database")
            .register(registry);

        startPolling();
    }

    private void startPolling() {
        ReactiveMongoDatabase db = mongoClient.getDatabase("Ratings");

        Thread pollingThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                db.getCollection("Rating", Document.class)
                    .countDocuments()
                    .subscribe()
                    .with(count -> {
                        ratingCount.set(count);
                        LOGGER.debugf("✅ Updated rating count: %d", count);
                    }, throwable -> LOGGER.error("❌ Failed to count ratings", throwable));

                try {
                    Thread.sleep(10_000); // poll every 10 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.warn("⏹️ Polling thread interrupted, stopping.");
                    break;
                }
            }
        }, "RatingMetricsPoller");

        pollingThread.setDaemon(true);
        pollingThread.start();
    }
}
