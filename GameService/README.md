# **Game Service (`/games`)**

## 📌 **Description**

The Game Service is responsible for storing and retrieving game-related data. It provides details about games and allows users to manage their Wish List.

## 🔗 **Technologies**

- **Language:** Go(GoKit)
- **Communication Type:** gRPC
- **Database:** MongoDB
- **Responsibilities:**
  - Store game information (title, genre, release date, developer, platform)
  - Allow users to retrieve details of games
  - Enable Wish List functionality (adding/removing games)

## 📂 **gRPC Methods**

- `GetGameById(id)` – Retrieve details of a game by its ID.
- `SearchGames(query)` – Search for games by title.
- `AddToWishList(userId, gameId)` – Add a game to the user's Wish List.
- `RemoveFromWishList(userId, gameId)` – Remove a game from the user's Wish List.
- `GetWishList(userId)` – Retrieve the user's Wish List.

## ✅ **TODO**

- Implement caching for frequently accessed game data.
- Integrate with external game databases (e.g., IGDB, RAWG API) for additional game metadata.
- Optimize search functionality for better query performance.
