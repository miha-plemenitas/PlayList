import { useEffect, useState } from "react";
import axios from "axios";
import "./App.css";

type Game = {
  id: number;
  title: string;
};

function App() {
  const [games, setGames] = useState<Game[]>([]);

  useEffect(() => {
    axios
      .get("http://localhost:8083/games") // via Web Gateway
      .then((res) => setGames(res.data))
      .catch((err) => console.error("Error loading games:", err));
  }, []);

  return (
    <div style={{ padding: "1rem", backgroundColor: "#f0f8ff" }}>
      <h2>🎮 Games Micro Frontend</h2>
      <ul>
        {games.map((game) => (
          <li key={game.id}>{game.title}</li>
        ))}
      </ul>
    </div>
  );
}

export default App;
