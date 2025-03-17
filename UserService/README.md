# **User Service (`/users`)**

## 📌 **Description**

The User Service manages user authentication, registration, and profile data. It ensures secure user access and stores relevant account details.

## 🔗 **Technologies**

- **Language:** Python (FastAPI) / C# (ASP.NET Core)
- **Communication Type:** REST API
- **Database:** MongoDB
- **Responsibilities:**
  - Handle user registration and authentication
  - Store and manage user profile information
  - Provide authentication tokens for secure API access

## 📂 **REST Endpoints**

- `POST /register` – Registers a new user.
- `POST /login` – Authenticates a user and returns a token.
- `GET /profile/{userId}` – Retrieves user profile data.
- `PUT /profile/{userId}` – Updates user profile information.
- `DELETE /profile/{userId}` – Deletes a user account.

## ✅ **TODO**

- Implement password reset functionality.
- Add OAuth integration (Google, Discord, etc.).
- Enable multi-factor authentication for enhanced security.
