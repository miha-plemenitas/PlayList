# Stage 1: Build static binary
FROM golang:1.22 AS builder

WORKDIR /app

COPY go.mod go.sum ./
RUN go mod download

COPY . .

# ✅ STATIC BUILD (no glibc dependency)
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o gameservice ./cmd/server

# Stage 2: Use lightweight final image
FROM gcr.io/distroless/static:nonroot

WORKDIR /app

COPY --from=builder /app/gameservice .

EXPOSE 50051

ENTRYPOINT ["/app/gameservice"]
