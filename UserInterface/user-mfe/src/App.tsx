import { useEffect, useState } from "react";
import axios from "axios";
import "./App.css";

type User = {
  id: number;
  username: string;
};

function App() {
  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    axios
      .get("http://localhost:8083/users") // via Web Gateway
      .then((res) => setUsers(res.data))
      .catch((err) => console.error("Error loading users:", err));
  }, []);

  return (
    <div style={{ padding: "1rem", backgroundColor: "#f9f9f9" }}>
      <h2>👤 User Micro Frontend</h2>
      <ul>
        {users.map((user) => (
          <li key={user.id}>{user.username}</li>
        ))}
      </ul>
    </div>
  );
}

export default App;
