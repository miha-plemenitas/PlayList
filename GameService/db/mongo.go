package db

import (
	"context"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

var (
	gameCollection     *mongo.Collection
	wishlistCollection *mongo.Collection
)

type Game struct {
	ID          string `bson:"id"`
	Title       string `bson:"title"`
	Genre       string `bson:"genre"`
	ReleaseDate string `bson:"release_date"`
	Developer   string `bson:"developer"`
	Platform    string `bson:"platform"`
}

type WishList struct {
	UserID  string   `bson:"user_id"`
	GameIDs []string `bson:"game_ids"`
}

func InitMongo(ctx context.Context, uri string, dbName string) (*mongo.Client, error) {
	clientOpts := options.Client().ApplyURI(uri)

	client, err := mongo.Connect(ctx, clientOpts)
	if err != nil {
		return nil, err
	}

	if err := client.Ping(ctx, nil); err != nil {
		return nil, err
	}

	db := client.Database(dbName)
	gameCollection = db.Collection("Games")
	wishlistCollection = db.Collection("Wishlists")

	return client, nil
}

// InsertGames inserts multiple game documents into the database
func InsertGames(ctx context.Context, games []interface{}) (*mongo.InsertManyResult, error) {
	return gameCollection.InsertMany(ctx, games)
}

// FindGameById finds a single game by its ID
func FindGameById(ctx context.Context, id string) (*Game, error) {
	filter := bson.M{"id": id}
	var game Game
	err := gameCollection.FindOne(ctx, filter).Decode(&game)
	if err != nil {
		return nil, err
	}
	return &game, nil
}

// SearchGamesByTitle searches games using a case-insensitive regex on title
func SearchGamesByTitle(ctx context.Context, query string) ([]*Game, error) {
	filter := bson.M{"title": primitive.Regex{Pattern: query, Options: "i"}}
	cursor, err := gameCollection.Find(ctx, filter)
	if err != nil {
		return nil, err
	}
	defer cursor.Close(ctx)

	var results []*Game
	for cursor.Next(ctx) {
		var game Game
		if err := cursor.Decode(&game); err != nil {
			return nil, err
		}
		results = append(results, &game)
	}
	return results, nil
}

// AddGameToWishlist adds a game to a user's wishlist (no duplicates)
func AddGameToWishlist(ctx context.Context, userId string, gameId string) error {
	filter := bson.M{"user_id": userId}
	update := bson.M{
		"$addToSet": bson.M{"game_ids": gameId},
	}
	_, err := wishlistCollection.UpdateOne(ctx, filter, update, options.Update().SetUpsert(true))
	return err
}

// RemoveGameFromWishlist removes a game from a user's wishlist
func RemoveGameFromWishlist(ctx context.Context, userId string, gameId string) error {
	filter := bson.M{"user_id": userId}
	update := bson.M{
		"$pull": bson.M{"game_ids": gameId},
	}
	_, err := wishlistCollection.UpdateOne(ctx, filter, update)
	return err
}

// GetWishlistGames retrieves all Game objects from the user's wishlist
func GetWishlistGames(ctx context.Context, userId string) ([]*Game, error) {
	var wl WishList
	err := wishlistCollection.FindOne(ctx, bson.M{"user_id": userId}).Decode(&wl)
	if err != nil {
		return nil, err
	}

	if len(wl.GameIDs) == 0 {
		return []*Game{}, nil
	}

	filter := bson.M{"id": bson.M{"$in": wl.GameIDs}}
	cursor, err := gameCollection.Find(ctx, filter)
	if err != nil {
		return nil, err
	}
	defer cursor.Close(ctx)

	var games []*Game
	for cursor.Next(ctx) {
		var game Game
		if err := cursor.Decode(&game); err != nil {
			return nil, err
		}
		games = append(games, &game)
	}

	return games, nil
}
