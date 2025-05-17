import { useEffect, useState } from "react";
import axios from "axios";
import "./App.css";

type UserProfile = {
  id: string;
  username: string;
  email: string;
};

function App() {
  const [email, setEmail] = useState("miha@gmail.com");
  const [password, setPassword] = useState("miha123");

  const [token, setToken] = useState<string | null>(
    localStorage.getItem("jwt")
  );
  const [userId, setUserId] = useState<string | null>(
    localStorage.getItem("userId")
  );
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [error, setError] = useState("");

  // Fetch profile after login (if we have both JWT and userId)
  useEffect(() => {
    const fetchProfile = async () => {
      if (!token || !userId) return;

      try {
        const res = await axios.get(
          `http://localhost:8083/users/profile/${userId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setProfile(res.data);
      } catch (err) {
        console.error("Error fetching profile:", err);
        setError("Session expired or token invalid.");
        setToken(null);
        localStorage.removeItem("jwt");
        localStorage.removeItem("userId");
      }
    };

    fetchProfile();
  }, [token, userId]);

  const handleLogin = async () => {
    try {
      const response = await axios.post("http://localhost:8083/users/login", {
        email,
        password,
      });

      const jwt = response.data;
      const hardcodedUserId = "6828e3edc772912dafc6b2e2"; // This comes from MongoDB

      setToken(jwt);
      setUserId(hardcodedUserId);
      localStorage.setItem("jwt", jwt);
      localStorage.setItem("userId", hardcodedUserId);
      setError("");
    } catch (err) {
      setError("Login failed or invalid credentials.");
      console.error(err);
    }
  };

  const handleLogout = () => {
    setToken(null);
    setUserId(null);
    setProfile(null);
    localStorage.removeItem("jwt");
    localStorage.removeItem("userId");
  };

  return (
    <div style={{ padding: "1rem", backgroundColor: "#fff8dc", color: "#333" }}>
      <h2>👤 User Micro Frontend</h2>

      {!token ? (
        <>
          <div>
            <label>Email: </label>
            <input
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              type="email"
            />
          </div>
          <div>
            <label>Password: </label>
            <input
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              type="password"
            />
          </div>
          <button onClick={handleLogin}>Login</button>
          {error && <p style={{ color: "red" }}>{error}</p>}
        </>
      ) : (
        <>
          {profile ? (
            <div>
              <h3>✅ Logged in as:</h3>
              <p>
                <strong>Username:</strong> {profile.username}
                <br />
                <strong>Email:</strong> {profile.email}
              </p>
              <button onClick={handleLogout}>Logout</button>
            </div>
          ) : (
            <p>Loading profile...</p>
          )}
        </>
      )}
    </div>
  );
}

export default App;
