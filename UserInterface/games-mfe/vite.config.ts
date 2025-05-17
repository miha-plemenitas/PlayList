import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import federation from "@originjs/vite-plugin-federation";

export default defineConfig({
  plugins: [
    react(),
    federation({
      name: "games_mfe",
      filename: "remoteEntry.js",
      exposes: {
        "./GamesApp": "./src/App.tsx",
      },
      shared: ["react", "react-dom"],
    }),
  ],
  define: {
    "process.env": {}, // ⬅️ Add this line
  },
  build: {
    target: "esnext",
    minify: false,
    cssCodeSplit: false,
    lib: {
      entry: "./src/main.tsx",
      formats: ["es"],
    },
  },
  server: {
    cors: true,
    headers: {
      "Access-Control-Allow-Origin": "*", // 👈 Critical for Module Federation
      "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, PATCH, OPTIONS",
      "Access-Control-Allow-Headers": "*",
    },
  },
});
