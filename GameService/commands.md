# COMMANDS

## FILL DATABASE

- go run fetch_and_seed.go

## SURCE ENV

- source .env

## RUN SERVER:

- go run cmd/server/main.go

## TEST GRPC:

- grpcurl -plaintext localhost:50051 list
- grpcurl -plaintext localhost:50051 list game.GameService

- ### GetGameById:

  - grpcurl -plaintext -d '{"id":"rawg-3498"}' -proto proto/game.proto localhost:50051 game.GameService/GetGameById

- ### SearchGames:

  - grpcurl -plaintext -d '{"query":"doom"}' -proto proto/game.proto localhost:50051 game.GameService/SearchGames

- ### AddToWishList:

  - grpcurl -plaintext -d '{"user_id":"miha", "game_id":"rawg-3498"}' -proto proto/game.proto localhost:50051 game.GameService/AddToWishList

- ### RemoveFromWishList:

  - grpcurl -plaintext -d '{"user_id":"miha", "game_id":"rawg-3498"}' -proto proto/game.proto localhost:50051 game.GameService/RemoveFromWishList

- ### GetWishList:

  - grpcurl -plaintext -d '{"user_id":"miha"}' -proto proto/game.proto localhost:50051 game.GameService/GetWishList

## TESTING:

- go test ./tests -v

## DOCKER:

- docker-compose down --volumes --remove-orphans
- docker-compose build
- docker-compose up

## METRICS

- http://localhost:2112/metrics
