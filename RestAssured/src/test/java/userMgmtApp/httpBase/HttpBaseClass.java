package userMgmtApp.httpBase;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpBaseClass {

    protected static final String URI = "http://localhost:8080";
    protected static final String REGISTRATION_ENDPOINT = "/register";
    protected static final String USERS_ENDPOINT = "/users/user/";

    @BeforeTest
    public void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    protected String getUserById(String uri, String userId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri.concat(userId))
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
    }
}
