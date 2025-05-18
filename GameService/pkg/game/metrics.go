package game

import (
	"context"
	"log"
	"net/http"
	"time"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
)

var (
    gameCountGauge = promauto.NewGauge(prometheus.GaugeOpts{
        Name: "games_total_in_db",
        Help: "Current number of games stored in MongoDB",
    })
)

func InitMetrics(client *mongo.Client) {
    http.Handle("/metrics", promhttp.Handler())

    // Start HTTP server for Prometheus metrics
    go func() {
        log.Println("📊 Prometheus metrics available at :2112/metrics")
        if err := http.ListenAndServe(":2112", nil); err != nil {
            log.Fatalf("Failed to start metrics endpoint: %v", err)
        }
    }()

    // Poll MongoDB for document count
    go func() {
        for {
            collection := client.Database("Games").Collection("Games")
            count, err := collection.CountDocuments(context.Background(), bson.M{})
            if err != nil {
                log.Printf("❌ Failed to count documents: %v", err)
            } else {
                gameCountGauge.Set(float64(count))
                log.Printf("✅ games_total_in_db updated to %d", count)
            }

            time.Sleep(10 * time.Second)
        }
    }()
}
