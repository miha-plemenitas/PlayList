package main

import (
	"log"

	"github.com/gofiber/fiber/v2"
)

func main() {
	app := fiber.New()

	// Routes
	app.Get("/api/users", func(c *fiber.Ctx) error {
		return c.SendString("Forward to User Service")
	})

	app.Get("/api/ratings", func(c *fiber.Ctx) error {
		return c.SendString("Forward to Rating Service")
	})

	// Start server
	if err := app.Listen(":3001"); err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}
