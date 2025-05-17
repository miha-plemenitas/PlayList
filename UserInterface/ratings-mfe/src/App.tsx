import "./App.css";
import { useEffect, useState } from "react";
import axios from "axios";

interface Rating {
  id: string;
  userId: string;
  gameId: string;
  rating: number;
}

function RatingsApp() {
  const [ratings, setRatings] = useState<Rating[]>([]);

  useEffect(() => {
    axios
      .get("http://localhost:8083/ratings/6828e3d28880fe4bb9d054b1") // Example gameId
      .then((res) => {
        setRatings(res.data);
      })
      .catch((err) => {
        console.error("Failed to fetch ratings:", err);
      });
  }, []);

  return (
    <div style={{ padding: "1rem", backgroundColor: "#fff8dc", color: "#333" }}>
      <h2>⭐ Ratings Micro Frontend</h2>
      <p>This is the ratings-specific MFE loaded via Module Federation.</p>

      <h3>Game Ratings:</h3>
      <ul>
        {ratings.map((r) => (
          <li key={r.id}>
            🧑‍💻 User: <code>{r.userId}</code> — ⭐ {r.rating}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default RatingsApp;
