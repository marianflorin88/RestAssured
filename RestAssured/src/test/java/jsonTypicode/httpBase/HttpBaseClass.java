package jsonTypicode.httpBase;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;

public class HttpBaseClass {
    protected static final String URI = "https://jsonplaceholder.typicode.com";
    protected static final String POSTS_ENDPOINT = "/posts";

    @BeforeTest
    public void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
