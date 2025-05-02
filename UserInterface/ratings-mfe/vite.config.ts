import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import federation from "@originjs/vite-plugin-federation";

export default defineConfig({
  plugins: [
    react(),
    federation({
      name: "ratings_mfe",
      filename: "remoteEntry.js",
      exposes: {
        "./RatingsApp": "./src/App.tsx",
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
});
