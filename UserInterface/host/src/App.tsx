import "./App.css";
import { lazy, Suspense } from "react";

const UserApp = lazy(() => import("user_mfe/UserApp"));
const GamesApp = lazy(() => import("games_mfe/GamesApp"));
const RatingsApp = lazy(() => import("ratings_mfe/RatingsApp"));

function App() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <h1 style={{ textAlign: "center" }}>🎯 Host App</h1>
      <UserApp />
      <GamesApp />
      <RatingsApp />
    </Suspense>
  );
}

export default App;
