import { useEffect, useState } from "react";
import axios from "axios";
import "./App.css";

type Game = {
  id: string;
  title: string;
  genre: string;
  release_date: string;
  developer: string;
  platform: string;
};

function GamesApp() {
  const [game, setGame] = useState<Game | null>(null);
  const [error, setError] = useState<string>("");

  useEffect(() => {
    axios
      .get("http://localhost:8082/games/rawg-4291")
      .then((res) => {
        setGame(res.data.game);
        setError("");
      })
      .catch((err) => {
        console.error("Error fetching game:", err);
        setError("Game not found.");
      });
  }, []);

  return (
    <div style={{ padding: "1rem", backgroundColor: "#fff8dc", color: "#333" }}>
      <h2>🎮 Game Details: rawg-4291</h2>

      {error && <p style={{ color: "red" }}>{error}</p>}

      {game && (
        <div>
          <h3>{game.title}</h3>
          <p>
            <strong>Genre:</strong> {game.genre}
            <br />
            <strong>Platform:</strong> {game.platform}
            <br />
            <strong>Developer:</strong> {game.developer}
            <br />
            <strong>Release Date:</strong> {game.release_date}
          </p>
        </div>
      )}
    </div>
  );
}

export default GamesApp;
