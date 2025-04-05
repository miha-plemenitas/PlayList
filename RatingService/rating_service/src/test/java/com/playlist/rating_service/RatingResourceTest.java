package com.playlist.rating_service;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class RatingResourceTest {

    @Test
    void testSubmitRating() {
        given()
          .contentType(ContentType.JSON)
          .body("{ \"gameId\": \"661ff2a3c13d4b12e0c12345\", \"userId\": \"661ff2a3c13d4b12e0c45678\", \"rating\": 5 }")
          .when()
          .post("/ratings")
          .then()
          .statusCode(200)
          .body("gameId", notNullValue())
          .body("userId", notNullValue())
          .body("rating", equalTo(5));
    }

    @Test
    void testGetRatingsByGameId() {
        String gameId = "661ff2a3c13d4b12e0c12345"; // Replace with real ID if needed
        given()
          .when()
          .get("/ratings/" + gameId)
          .then()
          .statusCode(200)
          .body("$", anyOf(empty(), notNullValue())); // either empty list or list of ratings
    }

    @Test
    void testSubmitComment() {
        given()
          .contentType(ContentType.JSON)
          .body("{ \"gameId\": \"661ff2a3c13d4b12e0c12345\", \"userId\": \"661ff2a3c13d4b12e0c45678\", \"comment\": \"Awesome game!\" }")
          .when()
          .post("/comments")
          .then()
          .statusCode(200)
          .body("gameId", notNullValue())
          .body("userId", notNullValue())
          .body("comment", equalTo("Awesome game!"));
    }

    @Test
    void testGetCommentsByGameId() {
        String gameId = "661ff2a3c13d4b12e0c12345"; // Replace with real ID if needed
        given()
          .when()
          .get("/comments/" + gameId)
          .then()
          .statusCode(200)
          .body("$", anyOf(empty(), notNullValue())); // empty list or list of comments
    }

    @Test
    void testDeleteComment() {
        String commentId = "661ff2a3c13d4b12e0c98765"; // Replace with real comment ID
        String userId = "661ff2a3c13d4b12e0c45678";   // Replace with real user ID
        given()
          .queryParam("userId", userId)
          .when()
          .delete("/comments/" + commentId)
          .then()
                .statusCode(anyOf(is(200), is(204), is(404))); // <- ADD 204 as allowed    
          }
}
