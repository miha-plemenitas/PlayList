# syntax=docker/dockerfile:1

FROM golang:1.24.1-alpine

WORKDIR /app

# Copy go.mod and go.sum
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy the rest of the app
COPY . .

# Build the app
RUN go build -o gameproxy

# Run the app
CMD ["./gameproxy"]
