package game

import (
	"context"
	"log"

	"github.com/miha-plemenitas/PlayList/GameService/db"
)

type Service interface {
	GetGameById(ctx context.Context, id string) (*db.Game, error)
	SearchGames(ctx context.Context, query string) ([]*db.Game, error)
	AddToWishList(ctx context.Context, userId string, gameId string) error
	RemoveFromWishList(ctx context.Context, userId string, gameId string) error
	GetWishList(ctx context.Context, userId string) ([]*db.Game, error)
}

type gameService struct {
	repo GameRepository
}

func NewGameService(repo GameRepository) Service {
	return &gameService{repo: repo}
}

func (s *gameService) GetGameById(ctx context.Context, id string) (*db.Game, error) {
	log.Printf("[GetGameById] Fetching game with ID: %s", id)
	return s.repo.FindGameById(ctx, id)
}

func (s *gameService) SearchGames(ctx context.Context, query string) ([]*db.Game, error) {
	log.Printf("[SearchGames] Searching games with query: '%s'", query)
	return s.repo.SearchGamesByTitle(ctx, query)
}

func (s *gameService) AddToWishList(ctx context.Context, userId string, gameId string) error {
	log.Printf("[AddToWishList] Adding game %s to user %s's wishlist", gameId, userId)
	return s.repo.AddGameToWishlist(ctx, userId, gameId)
}

func (s *gameService) RemoveFromWishList(ctx context.Context, userId string, gameId string) error {
	log.Printf("[RemoveFromWishList] Removing game %s from user %s's wishlist", gameId, userId)
	return s.repo.RemoveGameFromWishlist(ctx, userId, gameId)
}

func (s *gameService) GetWishList(ctx context.Context, userId string) ([]*db.Game, error) {
	log.Printf("[GetWishList] Fetching wishlist for user: %s", userId)
	return s.repo.GetWishlistGames(ctx, userId)
}
