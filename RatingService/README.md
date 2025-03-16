# **Rating Service (`/ratings`)**

## 📌 **Description**

The Rating Service handles user-generated ratings and comments for video games. It allows users to rate games, leave reviews, and retrieve community feedback.

## 🔗 **Technologies**

- **Communication Type:** Message Broker (e.g., RabbitMQ, Kafka)
- **Database:** MongoDB
- **Responsibilities:**
  - Allow users to submit ratings for games (1-10 scale)
  - Enable users to write and manage comments on games
  - Store and retrieve user-generated reviews

## 📂 **Endpoints / Message Topics**

- `SubmitRating(userId, gameId, rating)` – Allows users to submit a rating for a game.
- `GetRatings(gameId)` – Retrieves all ratings for a specific game.
- `SubmitComment(userId, gameId, comment)` – Adds a comment to a game.
- `GetComments(gameId)` – Retrieves all comments for a specific game.
- `DeleteComment(userId, commentId)` – Allows a user to delete their comment.

## ✅ **TODO**

- Implement moderation for inappropriate comments.
- Add aggregation features (average rating, most liked comments).
- Enable notifications for new comments or replies.
