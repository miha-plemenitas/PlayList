package main

import (
	"context"
	"fmt"
	"log"
	"net"
	"os"
	"os/signal"

	"github.com/miha-plemenitas/PlayList/GameService/db"
	"github.com/miha-plemenitas/PlayList/GameService/pkg/game"
	gamepb "github.com/miha-plemenitas/PlayList/GameService/proto"

	"google.golang.org/grpc"
	"google.golang.org/grpc/reflection"
)

type grpcServer struct {
	gamepb.UnimplementedGameServiceServer
	svc game.Service
}

func main() {
	// Setup context with cancel
	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt)
	defer stop()

	// MongoDB connection
	client, err := db.InitMongo(ctx, "mongodb+srv://Miha:miha123@playlistcluster.jy8zn.mongodb.net/", "Games")
	if err != nil {
		log.Fatalf("failed to connect to MongoDB: %v", err)
	}
	defer client.Disconnect(ctx)

	// Create service
	svc := game.NewGameService()

	// Create gRPC server
	server := grpc.NewServer()
	gamepb.RegisterGameServiceServer(server, &grpcServer{svc: svc})

	// Register reflection for grpcurl
	reflection.Register(server)

	// Listen on port 50051
	lis, err := net.Listen("tcp", ":50051")
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	fmt.Println("🎮 GameService gRPC server is running on port 50051...")

	if err := server.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}

// --- gRPC Method Implementations ---

func (s *grpcServer) GetGameById(ctx context.Context, req *gamepb.GameIdRequest) (*gamepb.GameResponse, error) {
	game, err := s.svc.GetGameById(ctx, req.GetId())
	if err != nil {
		return nil, err
	}
	return &gamepb.GameResponse{Game: convertToProtoGame(game)}, nil
}

func (s *grpcServer) SearchGames(ctx context.Context, req *gamepb.GameSearchRequest) (*gamepb.GameListResponse, error) {
	games, err := s.svc.SearchGames(ctx, req.GetQuery())
	if err != nil {
		return nil, err
	}
	return &gamepb.GameListResponse{Games: convertToProtoGames(games)}, nil
}

func (s *grpcServer) AddToWishList(ctx context.Context, req *gamepb.WishListRequest) (*gamepb.WishListResponse, error) {
	err := s.svc.AddToWishList(ctx, req.GetUserId(), req.GetGameId())
	if err != nil {
		return nil, err
	}
	return &gamepb.WishListResponse{Message: "Game added to wishlist"}, nil
}

func (s *grpcServer) RemoveFromWishList(ctx context.Context, req *gamepb.WishListRequest) (*gamepb.WishListResponse, error) {
	err := s.svc.RemoveFromWishList(ctx, req.GetUserId(), req.GetGameId())
	if err != nil {
		return nil, err
	}
	return &gamepb.WishListResponse{Message: "Game removed from wishlist"}, nil
}

func (s *grpcServer) GetWishList(ctx context.Context, req *gamepb.UserRequest) (*gamepb.GameListResponse, error) {
	games, err := s.svc.GetWishList(ctx, req.GetUserId())
	if err != nil {
		return nil, err
	}
	return &gamepb.GameListResponse{Games: convertToProtoGames(games)}, nil
}

// --- Conversion Helpers ---

func convertToProtoGame(g *db.Game) *gamepb.Game {
	if g == nil {
		return nil
	}
	return &gamepb.Game{
		Id:          g.ID,
		Title:       g.Title,
		Genre:       g.Genre,
		ReleaseDate: g.ReleaseDate,
		Developer:   g.Developer,
		Platform:    g.Platform,
	}
}

func convertToProtoGames(games []*db.Game) []*gamepb.Game {
	result := make([]*gamepb.Game, 0, len(games))
	for _, g := range games {
		result = append(result, convertToProtoGame(g))
	}
	return result
}
