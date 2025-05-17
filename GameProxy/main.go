package main

import (
	"context"
	"encoding/json"
	"log"
	"net/http"
	"time"

	pb "gameproxy/proto"

	"github.com/gorilla/mux"
	"github.com/rs/cors"
	"google.golang.org/grpc"

	_ "gameproxy/docs"

	httpSwagger "github.com/swaggo/http-swagger"
)

// @title Game Proxy API
// @version 1.0
// @description REST Proxy for gRPC GameService
// @host localhost:8082
// @BasePath /

// @contact.name API Support
// @contact.email support@example.com

var grpcClient pb.GameServiceClient

func main() {
	// Connect to gRPC GameService
	conn, err := grpc.Dial("playlist-gameservice:50051", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("Failed to connect to GameService: %v", err)
	}
	defer conn.Close()
	grpcClient = pb.NewGameServiceClient(conn)

	// Set up HTTP routes
	r := mux.NewRouter()

	// Swagger docs
	r.PathPrefix("/swagger/").Handler(httpSwagger.WrapHandler)

	r.HandleFunc("/games/{id}", handleGetGameById).Methods("GET")
	r.HandleFunc("/games/search", handleSearchGames).Methods("GET")
	r.HandleFunc("/wishlist/add", handleAddToWishList).Methods("POST")
	r.HandleFunc("/wishlist/remove", handleRemoveFromWishList).Methods("POST")
	r.HandleFunc("/wishlist", handleGetWishList).Methods("GET")

	// Add CORS support
	handler := cors.New(cors.Options{
		AllowedOrigins:   []string{"*"}, // or restrict to your frontend: []string{"http://localhost:3000"}
		AllowedMethods:   []string{"GET", "POST", "OPTIONS"},
		AllowedHeaders:   []string{"*"},
		AllowCredentials: true,
	}).Handler(r)

	log.Println("REST proxy listening on port 8082")
	log.Fatal(http.ListenAndServe(":8082", handler))
}

// @Summary Get Game by ID
// @Param id path string true "Game ID"
// @Success 200 {object} pb.GameResponse
// @Failure 500 {string} string
// @Router /games/{id} [get]
func handleGetGameById(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	id := vars["id"]

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	resp, err := grpcClient.GetGameById(ctx, &pb.GameIdRequest{Id: id})
	if err != nil {
		http.Error(w, "Failed to fetch game: "+err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// @Summary Search Games
// @Param q query string true "Search Query"
// @Success 200 {object} pb.GameListResponse
// @Failure 500 {string} string
// @Router /games/search [get]
func handleSearchGames(w http.ResponseWriter, r *http.Request) {
	query := r.URL.Query().Get("q")

	if query == "" {
		http.Error(w, "Missing query parameter 'q'", http.StatusBadRequest)
		return
	}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	resp, err := grpcClient.SearchGames(ctx, &pb.GameSearchRequest{Query: query})
	if err != nil {
		http.Error(w, "Failed to fetch game: "+err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// @Summary Add Game to Wishlist
// @Accept json
// @Produce json
// @Param body body pb.WishListRequest true "Wishlist Add Request"
// @Success 200 {object} pb.WishListResponse
// @Failure 500 {string} string
// @Router /wishlist/add [post]
func handleAddToWishList(w http.ResponseWriter, r *http.Request) {
	var req pb.WishListRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	resp, err := grpcClient.AddToWishList(ctx, &req)
	if err != nil {
		http.Error(w, "Failed to add to wishlist: "+err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// @Summary Remove Game from Wishlist
// @Accept json
// @Produce json
// @Param body body pb.WishListRequest true "Wishlist Remove Request"
// @Success 200 {object} pb.WishListResponse
// @Failure 500 {string} string
// @Router /wishlist/remove [post]
func handleRemoveFromWishList(w http.ResponseWriter, r *http.Request) {
	var req pb.WishListRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	resp, err := grpcClient.RemoveFromWishList(ctx, &req)
	if err != nil {
		http.Error(w, "Failed to remove from wishlist: "+err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// @Summary Get Wishlist for User
// @Param user_id query string true "User ID"
// @Success 200 {object} pb.GameListResponse
// @Failure 500 {string} string
// @Router /wishlist [get]
func handleGetWishList(w http.ResponseWriter, r *http.Request) {
	userID := r.URL.Query().Get("user_id")

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	resp, err := grpcClient.GetWishList(ctx, &pb.UserRequest{UserId: userID})
	if err != nil {
		http.Error(w, "Failed to get wishlist: "+err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}
