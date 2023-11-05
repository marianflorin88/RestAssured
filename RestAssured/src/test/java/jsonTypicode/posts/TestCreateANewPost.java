package jsonTypicode.posts;

import Data.jsonTypicode.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import jsonTypicode.httpBase.HttpBaseClass;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TestCreateANewPost extends HttpBaseClass {

    private static final String API_ROUTE = URI.concat(POSTS_ENDPOINT);

    @Test
    public void testCreateANewPost() throws JsonProcessingException {
        String requestUserId = "75";
        String requestPostTitle = "Fortech Academy title updated";
        String requestPostBody = "RestAssured Request Body";

        Post postBody = new Post(requestUserId, requestPostTitle, requestPostBody);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(postBody);

        Response res = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(API_ROUTE);

        ValidatableResponse v = res.then();

        v.statusCode(201);
        v.time(Matchers.lessThan(2000L));
        v.body(Matchers.notNullValue());

        String responseUserId = v.extract().path("userId");
        String responseTitle = v.extract().path("title");
        String responseBody = v.extract().path("body");
        int responsePostId = v.extract().path("id");

        Assert.assertEquals(requestUserId, responseUserId);
        Assert.assertEquals(requestPostTitle, responseTitle);
        Assert.assertEquals(requestPostBody, responseBody);
        Assert.assertEquals(responsePostId, 101);
    }
}
