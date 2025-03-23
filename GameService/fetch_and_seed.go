package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"

	"github.com/miha-plemenitas/PlayList/GameService/db"
)

const (
	apiKey  = "bb97fce6cf4b4d59bd67f516c47b9ee6"
	baseURL = "https://api.rawg.io/api/games"
)

type RawgGameListResponse struct {
	Results []struct {
		ID         int    `json:"id"`
		Name       string `json:"name"`
		Released   string `json:"released"`
		Genres     []struct{ Name string } `json:"genres"`
		Platforms  []PlatformWrapper       `json:"platforms"`
	} `json:"results"`
}

type PlatformWrapper struct {
	Platform struct {
		Name string `json:"name"`
	} `json:"platform"`
}

type RawgGameDetails struct {
	ID         int    `json:"id"`
	Name       string `json:"name"`
	Released   string `json:"released"`
	Genres     []struct{ Name string } `json:"genres"`
	Developers []struct{ Name string } `json:"developers"`
	Platforms  []PlatformWrapper       `json:"platforms"`
}

func main() {
	ctx, cancel := context.WithTimeout(context.Background(), 60*time.Second)
	defer cancel()

	client, err := db.InitMongo(ctx, "mongodb+srv://Miha:miha123@playlistcluster.jy8zn.mongodb.net/", "Games")
	if err != nil {
		log.Fatalf("❌ MongoDB connection error: %v", err)
	}
	defer client.Disconnect(ctx)

	var games []interface{}

	for page := 1; page <= 2; page++ {
		url := fmt.Sprintf("%s?key=%s&page_size=40&page=%d", baseURL, apiKey, page)
		resp, err := http.Get(url)
		if err != nil {
			log.Fatalf("❌ Failed to fetch games list: %v", err)
		}
		defer resp.Body.Close()

		var list RawgGameListResponse
		if err := json.NewDecoder(resp.Body).Decode(&list); err != nil {
			log.Fatalf("❌ Failed to decode game list: %v", err)
		}

		for _, g := range list.Results {
			detail, err := fetchGameDetails(g.ID)
			if err != nil {
				log.Printf("⚠️ Skipping game %d due to detail fetch error: %v", g.ID, err)
				continue
			}

			game := db.Game{
				ID:          fmt.Sprintf("rawg-%d", detail.ID),
				Title:       detail.Name,
				Genre:       flattenGenres(detail.Genres),
				ReleaseDate: detail.Released,
				Developer:   firstOrUnknown(detail.Developers),
				Platform:    firstPlatformOrUnknown(detail.Platforms),
			}
			games = append(games, game)
		}
	}

	if len(games) == 0 {
		log.Println("⚠️ No games to insert.")
		return
	}

	res, err := db.InsertGames(ctx, games)
	if err != nil {
		log.Fatalf("❌ Insert error: %v", err)
	}
	fmt.Printf("✅ Inserted %d games\n", len(res.InsertedIDs))
}

func fetchGameDetails(id int) (*RawgGameDetails, error) {
	url := fmt.Sprintf("https://api.rawg.io/api/games/%d?key=%s", id, apiKey)
	resp, err := http.Get(url)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	var detail RawgGameDetails
	if err := json.NewDecoder(resp.Body).Decode(&detail); err != nil {
		return nil, err
	}
	return &detail, nil
}

func flattenGenres(genres []struct{ Name string }) string {
	if len(genres) == 0 {
		return "Unknown"
	}
	return genres[0].Name
}

func firstOrUnknown(devs []struct{ Name string }) string {
	if len(devs) > 0 {
		return devs[0].Name
	}
	return "Unknown"
}

func firstPlatformOrUnknown(platforms []PlatformWrapper) string {
	if len(platforms) > 0 {
		return platforms[0].Platform.Name
	}
	return "Unknown"
}
