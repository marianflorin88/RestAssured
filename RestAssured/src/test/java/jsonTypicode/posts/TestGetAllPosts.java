package jsonTypicode.posts;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import jsonTypicode.httpBase.HttpBaseClass;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TestGetAllPosts extends HttpBaseClass {

    private static final String API_ROUTE = URI.concat(POSTS_ENDPOINT);

    @Test
    public void testGetAllPosts() {

        Response res = given()
                .contentType("application/json")
                .when()
                .get(API_ROUTE);

        ValidatableResponse v = res.then();

        v.statusCode(200);
        v.time(Matchers.lessThan(1000L));
        v.body(Matchers.notNullValue());

    }
}
