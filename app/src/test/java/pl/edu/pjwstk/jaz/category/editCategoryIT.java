package pl.edu.pjwstk.jaz.category;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.IntegrationTest;
import pl.edu.pjwstk.jaz.controllers.requests.CategoryRequest;
import pl.edu.pjwstk.jaz.controllers.requests.LoginRequest;
import pl.edu.pjwstk.jaz.controllers.requests.RegisterRequest;
import pl.edu.pjwstk.jaz.controllers.requests.SectionRequest;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@IntegrationTest
public class editCategoryIT {

    private static Response adminResponse;
    private static Response authenticatedUser;


    @BeforeClass
    public static void setUp(){
        adminResponse = given()
                .when()
                .body(new LoginRequest("admin", "admin"))
                .contentType(ContentType.JSON)
                .post("/api/login")
                .thenReturn();
        given()
                .when()
                .body(new RegisterRequest("testUser", "user"))
                .contentType(ContentType.JSON)
                .post("/api/register");
        authenticatedUser = given()
                .when()
                .body(new LoginRequest("testUser", "user"))
                .contentType(ContentType.JSON)
                .post("/api/login")
                .thenReturn();
    }

    @AfterClass
    public static void tearDown(){
        SectionRequest categoryRequest = new SectionRequest();
        categoryRequest.setName("editTestCategory");
        given()
                .cookies(adminResponse.getCookies())
                .contentType(ContentType.JSON)
                .body(categoryRequest)
                .put("/api/section/1")
                .then()
                .statusCode(HttpStatus.OK.value());
    }


    @Test
    public void unauthenticated_user_should_not_have_access_403(){
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("editTestCategory");
        categoryRequest.setSection("Żelazo");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(categoryRequest)
                .put("/api/category/1")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void authenticated_user_should_not_have_access_403(){
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("editTestCategory");
        categoryRequest.setSection("Żelazo");
        given()
                .cookies(authenticatedUser.getCookies())
                .contentType(ContentType.JSON)
                .body(categoryRequest)
                .put("/api/category/1")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void admin_should_be_able_to_edit_category_200(){
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("editTestCategory");
        categoryRequest.setSection("Żelazo");
        given()
                .cookies(adminResponse.getCookies())
                .contentType(ContentType.JSON)
                .body(categoryRequest)
                .put("/api/category/1")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

}
