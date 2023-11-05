package userMgmtApp.crud;

import Data.userMgmtApp.UserAccount;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;
import userMgmtApp.httpBase.HttpBaseClass;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.*;

public class TestUser extends HttpBaseClass {

    private static final String API_ROUTE_REGISTRATION = URI.concat(REGISTRATION_ENDPOINT);
    private static final String API_ROUTE_USERS = URI.concat(USERS_ENDPOINT);
    private static String userId;

    @Test(priority = 1, description = "Creates a new user account on the platform")
    public void testCreateANewUser() {
        UserAccount userAccount = new UserAccount("testFn", "testLn", "testFn-testLn@yopmail.com", "+40740123456");

        JSONObject requestBody = new JSONObject();
        requestBody.put("firstName", userAccount.getFirstName());
        requestBody.put("lastName", userAccount.getLastName());
        requestBody.put("email", userAccount.getEmail());
        requestBody.put("phoneNumber", userAccount.getPhoneNumber());

        String res = given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .post(API_ROUTE_REGISTRATION)
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .asString();

        System.out.println(res);

        JSONObject jsonResponse = new JSONObject(res);
        assertThat(jsonResponse.getBoolean("error"), Matchers.is(false));
        assertThat(jsonResponse.getString("message"), Matchers.matchesPattern("User with email address .* has been successfully registered."));
        assertThat(jsonResponse.getInt("userId"), Matchers.greaterThan(0));
        JsonPath jsonPath = new JsonPath(res);
        userId = jsonPath.getString("userId");
        System.out.println(userId);
    }

    @Test(priority = 2, description = "Validates user was created successfully")
    public void testGetNewUserById() {
        String res = given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_ROUTE_USERS.concat(userId))
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();

        JsonPath jsonPath = new JsonPath(res);
        assertThat(jsonPath.getString("userId"), Matchers.is(userId));
        System.out.println(userId);
    }

    @Test(priority = 3, description = "Updates user account on the platform")
    public void testUpdateUser() {
        UserAccount userAccount = new UserAccount("updatedFn", "updatedLn", "updatedFn-updatedLn@yopmail.com", "+40123456789");
        final String UPDATE_ENDPOINT = userId.concat("/update");
        final String API_ROUTE_USER_UPDATE = API_ROUTE_USERS.concat(UPDATE_ENDPOINT);

        JSONObject requestBody = new JSONObject();
        requestBody.put("firstName", userAccount.getFirstName());
        requestBody.put("lastName", userAccount.getLastName());
        requestBody.put("email", userAccount.getEmail());
        requestBody.put("phoneNumber", userAccount.getPhoneNumber());

        String res = given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .put(API_ROUTE_USER_UPDATE)
                .then()
                .extract()
                .asString();

        JSONObject jsonResponse = new JSONObject(res);
        JsonPath jsonPath = new JsonPath(res);
        assertThat(jsonResponse.getBoolean("error"), Matchers.is(false));
        assertThat(jsonResponse.getString("message"), Matchers.matchesPattern("User with ID .* has been successfully updated."));
        assertThat(jsonPath.getString("userId"), Matchers.is(userId));
    }

    @Test(priority = 4, description = "Validates user was created successfully")
    public void testConfirmUserUpdatedWithGet() {
        String res = getUserById(API_ROUTE_USERS, userId);
        JsonPath jsonPath = new JsonPath(res);
        assertThat(jsonPath.getString("userId"), Matchers.is(userId));
        System.out.println(userId);
    }

    @Test(priority = 5, description = "Deletes user account")
    public void testDeleteUser() {
        final String DELETE_ENDPOINT = userId.concat("/delete");
        final String API_ROUTE_USER_DELETE = API_ROUTE_USERS.concat(DELETE_ENDPOINT);

        String res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete(API_ROUTE_USER_DELETE)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();

        JSONObject jsonResponse = new JSONObject(res);
        assertThat(jsonResponse.getBoolean("error"), Matchers.is(false));
        assertThat(jsonResponse.getString("message"), Matchers.matchesPattern("User with ID .* has been successfully deleted."));
    }

}