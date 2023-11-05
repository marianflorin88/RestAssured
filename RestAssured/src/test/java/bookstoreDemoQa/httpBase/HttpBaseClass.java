package bookstoreDemoQa.httpBase;

import io.restassured.RestAssured;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;

public class HttpBaseClass {
    protected static final String URI = "https://demoqa.com";
    protected static final String ACCOUNT_ENDPOINT = "/Account/v1";

    protected static final String BOOKSTORE_ENDPOINT = "/BookStore/v1";

    @BeforeTest
    public void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public static String findIsbnByTitle(JSONArray booksArray, String titleToFind) {
        for (int i = 0; i < booksArray.length(); i++) {
            JSONObject book = booksArray.getJSONObject(i);
            String title = book.getString("title");
            if (title.equals(titleToFind)) {
                return book.getString("isbn");
            }
        }
        return null;
    }
}
