package bookstoreDemoQa.Account;

import bookstoreDemoQa.httpBase.HttpBaseClass;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestUserLogin extends HttpBaseClass {
    private static final String API_RROUTE = URI.concat(ACCOUNT_ENDPOINT);
    private static final String API_ROUTE_BOOKS = URI.concat(BOOKSTORE_ENDPOINT);
    private static String userId;
    private static String token;
    private static String isbn;

    @Test(priority = 1, description ="Login with valid username and password")
    public void testLoginWithValidCredetianls(){
        final String API_ROUTE_LOGIN = API_RROUTE.concat("/Login");
        String username = "1023231";
        String password = "Test@123";
        JSONObject requestBody = new JSONObject();
        requestBody.put("userName",username);
        requestBody.put("password",password);

        String res = given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .post(API_ROUTE_LOGIN)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();

        System.out.println(res);
        JSONObject jsonResponse = new JSONObject(res);
        assertThat(jsonResponse.getString("username"), Matchers.is(username));
        JsonPath jsonPath = new JsonPath(res);
        userId = jsonPath.getString("userId");
        token = jsonPath.getString("token");
    }

    @Test(priority = 2, description ="GET all books")
    public void testGetAllBooksForUser() {
        final String API_ROUTE = API_ROUTE_BOOKS.concat("/Books");
        final String titleToFind = "Designing Evolvable Web APIs with ASP.NET";

        Response res = given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_ROUTE);

        ValidatableResponse v = res.then();
        v.statusCode(200);

        String jsonResponse = res.asString();
        JSONObject jsonObject = new JSONObject(jsonResponse);

        JSONArray booksArray = jsonObject.getJSONArray("books");
        isbn = findIsbnByTitle(booksArray, titleToFind);

    }

    @Test( priority = 3,description = "Add a book with ISBN to user collection")
    public void addBookToCollection(){
        final String API_ROUTE = API_ROUTE_BOOKS.concat("/Books");
        Response res = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(" {\"userId\":\"" + userId + "\"," +
                        "\"collectionOfIsbns\":[{\"isbn\":\"" + isbn + "\"}]}"
                )
                .when()
                .post(API_ROUTE);

        ValidatableResponse v = res.then();
        v.statusCode(201);
    }
    @Test(priority = 4, description = "Delete Book from collection" )
    public void testDeleteBookFromCollection(){
        final String API_ROUTE = API_ROUTE_BOOKS.concat("/Book");
        Response res = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(" {\"userId\":\"" + userId + "\"," +
                        "\"isbn\":\"" + isbn + "\"}"
                )
                .when()
                .delete(API_ROUTE);

        ValidatableResponse v = res.then();
        v.statusCode(204);

    }

}
