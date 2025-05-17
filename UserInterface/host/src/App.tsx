import "./App.css";
import { lazy, Suspense } from "react";

const UserApp = lazy(() => import("user_mfe/UserApp"));
const GamesApp = lazy(() => import("games_mfe/GamesApp"));
const RatingsApp = lazy(() => import("ratings_mfe/RatingsApp"));

function App() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <h1 style={{ textAlign: "center", color: "#eee", fontSize: "2rem" }}>
        🎯 Host App
      </h1>
      <div
        style={{
          maxWidth: "900px",
          margin: "0 auto",
          backgroundColor: "#fefefe",
          padding: "1.5rem",
          borderRadius: "12px",
          boxShadow: "0 4px 10px rgba(0, 0, 0, 0.1)",
        }}
      >
        <UserApp />
        <div
          style={{
            backgroundColor: "#e8f4ff",
            padding: "1rem",
            borderRadius: "8px",
            marginTop: "1rem",
          }}
        >
          <GamesApp />
        </div>
        <div
          style={{
            backgroundColor: "#fffbe6",
            padding: "1rem",
            borderRadius: "8px",
            marginTop: "1rem",
          }}
        >
          <RatingsApp />
        </div>
      </div>
    </Suspense>
  );
}

export default App;
