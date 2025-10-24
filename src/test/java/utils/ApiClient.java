package utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Comment;
import models.Post;
import models.User;

import static io.restassured.RestAssured.given;

/**
 * Централизованный клиент для API вызовов
 */
public class ApiClient {

    // ==================== POSTS API ====================

    @Step("Получить пост по ID: {postId}")
    public static Response getPost(int postId) {
        return given()
            .when()
            .get("/posts/" + postId)
            .then()
            .extract().response();
    }

    @Step("Получить все посты")
    public static Response getAllPosts() {
        return given()
            .when()
            .get("/posts")
            .then()
            .extract().response();
    }

    @Step("Создать новый пост")
    public static Response createPost(Post post) {
        return given()
            .contentType("application/json")
            .body(post)
            .when()
            .post("/posts")
            .then()
            .extract().response();
    }

    @Step("Обновить пост с ID: {postId}")
    public static Response updatePost(int postId, Post post) {
        return given()
            .contentType("application/json")
            .body(post)
            .when()
            .put("/posts/" + postId)
            .then()
            .extract().response();
    }

    @Step("Удалить пост с ID: {postId}")
    public static Response deletePost(int postId) {
        return given()
            .when()
            .delete("/posts/" + postId)
            .then()
            .extract().response();
    }

    @Step("Получить посты пользователя с ID: {userId}")
    public static Response getPostsByUserId(int userId) {
        return given()
            .queryParam("userId", userId)
            .when()
            .get("/posts")
            .then()
            .extract().response();
    }

    // ==================== USERS API ====================

    @Step("Получить пользователя по ID: {userId}")
    public static Response getUser(int userId) {
        return given()
            .when()
            .get("/users/" + userId)
            .then()
            .extract().response();
    }

    @Step("Получить всех пользователей")
    public static Response getAllUsers() {
        return given()
            .when()
            .get("/users")
            .then()
            .extract().response();
    }

    @Step("Создать нового пользователя")
    public static Response createUser(User user) {
        return given()
            .contentType("application/json")
            .body(user)
            .when()
            .post("/users")
            .then()
            .extract().response();
    }

    @Step("Обновить пользователя с ID: {userId}")
    public static Response updateUser(int userId, User user) {
        return given()
            .contentType("application/json")
            .body(user)
            .when()
            .put("/users/" + userId)
            .then()
            .extract().response();
    }

    @Step("Удалить пользователя с ID: {userId}")
    public static Response deleteUser(int userId) {
        return given()
            .when()
            .delete("/users/" + userId)
            .then()
            .extract().response();
    }

    // ==================== COMMENTS API ====================

    @Step("Получить комментарий по ID: {commentId}")
    public static Response getComment(int commentId) {
        return given()
            .when()
            .get("/comments/" + commentId)
            .then()
            .extract().response();
    }

    @Step("Получить все комментарии")
    public static Response getAllComments() {
        return given()
            .when()
            .get("/comments")
            .then()
            .extract().response();
    }

    @Step("Создать новый комментарий")
    public static Response createComment(Comment comment) {
        return given()
            .contentType("application/json")
            .body(comment)
            .when()
            .post("/comments")
            .then()
            .extract().response();
    }

    @Step("Обновить комментарий с ID: {commentId}")
    public static Response updateComment(int commentId, Comment comment) {
        return given()
            .contentType("application/json")
            .body(comment)
            .when()
            .put("/comments/" + commentId)
            .then()
            .extract().response();
    }

    @Step("Удалить комментарий с ID: {commentId}")
    public static Response deleteComment(int commentId) {
        return given()
            .when()
            .delete("/comments/" + commentId)
            .then()
            .extract().response();
    }

    @Step("Получить комментарии к посту с ID: {postId}")
    public static Response getCommentsByPostId(int postId) {
        return given()
            .queryParam("postId", postId)
            .when()
            .get("/comments")
            .then()
            .extract().response();
    }

    // ==================== ALBUMS API ====================

    @Step("Получить альбом по ID: {albumId}")
    public static Response getAlbum(int albumId) {
        return given()
            .when()
            .get("/albums/" + albumId)
            .then()
            .extract().response();
    }

    @Step("Получить все альбомы")
    public static Response getAllAlbums() {
        return given()
            .when()
            .get("/albums")
            .then()
            .extract().response();
    }

    @Step("Получить альбомы пользователя с ID: {userId}")
    public static Response getAlbumsByUserId(int userId) {
        return given()
            .queryParam("userId", userId)
            .when()
            .get("/albums")
            .then()
            .extract().response();
    }

    // ==================== PHOTOS API ====================

    @Step("Получить фото по ID: {photoId}")
    public static Response getPhoto(int photoId) {
        return given()
            .when()
            .get("/photos/" + photoId)
            .then()
            .extract().response();
    }

    @Step("Получить все фото")
    public static Response getAllPhotos() {
        return given()
            .when()
            .get("/photos")
            .then()
            .extract().response();
    }

    @Step("Получить фото из альбома с ID: {albumId}")
    public static Response getPhotosByAlbumId(int albumId) {
        return given()
            .queryParam("albumId", albumId)
            .when()
            .get("/photos")
            .then()
            .extract().response();
    }

    // ==================== TODOS API ====================

    @Step("Получить задачу по ID: {todoId}")
    public static Response getTodo(int todoId) {
        return given()
            .when()
            .get("/todos/" + todoId)
            .then()
            .extract().response();
    }

    @Step("Получить все задачи")
    public static Response getAllTodos() {
        return given()
            .when()
            .get("/todos")
            .then()
            .extract().response();
    }

    @Step("Получить задачи пользователя с ID: {userId}")
    public static Response getTodosByUserId(int userId) {
        return given()
            .queryParam("userId", userId)
            .when()
            .get("/todos")
            .then()
            .extract().response();
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    /**
     * Проверить, что ответ успешный
     */
    public static boolean isSuccessResponse(Response response) {
        return response.getStatusCode() >= 200 && response.getStatusCode() < 300;
    }

    /**
     * Получить ID из ответа
     */
    public static int getIdFromResponse(Response response) {
        return response.jsonPath().getInt("id");
    }

    /**
     * Получить размер массива из ответа
     */
    public static int getArraySizeFromResponse(Response response) {
        return response.jsonPath().getList("").size();
    }

    /**
     * Проверить, что ответ содержит поле
     */
    public static boolean responseContainsField(Response response, String fieldName) {
        try {
            response.jsonPath().getString(fieldName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получить значение поля из ответа
     */
    public static String getFieldValue(Response response, String fieldName) {
        return response.jsonPath().getString(fieldName);
    }

    /**
     * Создать тестовый пост
     */
    public static Post createTestPost(int userId, String title, String body) {
        return Post.builder()
            .userId(userId)
            .title(title)
            .body(body)
            .build();
    }

    /**
     * Создать тестовый комментарий
     */
    public static Comment createTestComment(int postId, String name, String email, String body) {
        return Comment.builder()
            .postId(postId)
            .name(name)
            .email(email)
            .body(body)
            .build();
    }

    /**
     * Создать тестового пользователя
     */
    public static User createTestUser(String name, String username, String email) {
        return User.builder()
            .name(name)
            .username(username)
            .email(email)
            .build();
    }
}


