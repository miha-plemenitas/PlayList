package game

import (
	"context"

	"github.com/miha-plemenitas/PlayList/GameService/db"
)

type GameRepository interface {
	FindGameById(ctx context.Context, id string) (*db.Game, error)
	SearchGamesByTitle(ctx context.Context, query string) ([]*db.Game, error)
	AddGameToWishlist(ctx context.Context, userId, gameId string) error
	RemoveGameFromWishlist(ctx context.Context, userId, gameId string) error
	GetWishlistGames(ctx context.Context, userId string) ([]*db.Game, error)
}
