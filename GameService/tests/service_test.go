package tests

import (
	"context"
	"errors"
	"testing"

	"github.com/miha-plemenitas/PlayList/GameService/db"
	"github.com/miha-plemenitas/PlayList/GameService/pkg/game"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

// --- MOCK REPO IMPLEMENTATION ---

type mockRepo struct {
	mock.Mock
}

func (m *mockRepo) FindGameById(ctx context.Context, id string) (*db.Game, error) {
	args := m.Called(ctx, id)
	return args.Get(0).(*db.Game), args.Error(1)
}

func (m *mockRepo) SearchGamesByTitle(ctx context.Context, query string) ([]*db.Game, error) {
	args := m.Called(ctx, query)
	return args.Get(0).([]*db.Game), args.Error(1)
}

func (m *mockRepo) AddGameToWishlist(ctx context.Context, userId, gameId string) error {
	args := m.Called(ctx, userId, gameId)
	return args.Error(0)
}

func (m *mockRepo) RemoveGameFromWishlist(ctx context.Context, userId, gameId string) error {
	args := m.Called(ctx, userId, gameId)
	return args.Error(0)
}

func (m *mockRepo) GetWishlistGames(ctx context.Context, userId string) ([]*db.Game, error) {
	args := m.Called(ctx, userId)
	return args.Get(0).([]*db.Game), args.Error(1)
}

// --- TESTS ---

func TestGetGameById_Success(t *testing.T) {
	mockRepo := new(mockRepo)
	svc := game.NewGameService(mockRepo)

	expectedGame := &db.Game{
		ID:    "game-123",
		Title: "Test Game",
	}

	mockRepo.On("FindGameById", mock.Anything, "game-123").Return(expectedGame, nil)

	got, err := svc.GetGameById(context.Background(), "game-123")

	assert.NoError(t, err)
	assert.Equal(t, expectedGame, got)
	mockRepo.AssertExpectations(t)
}

func TestGetGameById_NotFound(t *testing.T) {
	mockRepo := new(mockRepo)
	svc := game.NewGameService(mockRepo)

	// 👇 key fix here!
	mockRepo.On("FindGameById", mock.Anything, "missing").Return((*db.Game)(nil), errors.New("not found"))

	got, err := svc.GetGameById(context.Background(), "missing")

	assert.Nil(t, got)
	assert.EqualError(t, err, "not found")
	mockRepo.AssertExpectations(t)
}

func TestSearchGames(t *testing.T) {
	mockRepo := new(mockRepo)
	svc := game.NewGameService(mockRepo)

	expected := []*db.Game{
		{ID: "1", Title: "Game 1"},
		{ID: "2", Title: "Game 2"},
	}
	mockRepo.On("SearchGamesByTitle", mock.Anything, "Game").Return(expected, nil)

	got, err := svc.SearchGames(context.Background(), "Game")

	assert.NoError(t, err)
	assert.Equal(t, expected, got)
	mockRepo.AssertExpectations(t)
}

func TestAddToWishlist(t *testing.T) {
	mockRepo := new(mockRepo)
	svc := game.NewGameService(mockRepo)

	mockRepo.On("AddGameToWishlist", mock.Anything, "user1", "game1").Return(nil)

	err := svc.AddToWishList(context.Background(), "user1", "game1")

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestRemoveFromWishlist(t *testing.T) {
	mockRepo := new(mockRepo)
	svc := game.NewGameService(mockRepo)

	mockRepo.On("RemoveGameFromWishlist", mock.Anything, "user1", "game1").Return(nil)

	err := svc.RemoveFromWishList(context.Background(), "user1", "game1")

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestGetWishlist(t *testing.T) {
	mockRepo := new(mockRepo)
	svc := game.NewGameService(mockRepo)

	expected := []*db.Game{
		{ID: "1", Title: "Game 1"},
	}
	mockRepo.On("GetWishlistGames", mock.Anything, "user1").Return(expected, nil)

	got, err := svc.GetWishList(context.Background(), "user1")

	assert.NoError(t, err)
	assert.Equal(t, expected, got)
	mockRepo.AssertExpectations(t)
}
