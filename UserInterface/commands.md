### MICRO-FRONTENDS

- cd UserInterface/user-mfe
  npm run build
  npx serve -s dist -l 4174

- cd UserInterface/games-mfe
  npm run build
  npx serve -s dist -l 4175

- cd UserInterface/ratings-mfe
  npm run build
  npx serve -s dist -l 4176

- cd UserInterface/host
  npm run dev -- --port 5173
