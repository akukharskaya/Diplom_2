package praktikum;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;
import praktikum.service.UserService;

import static org.hamcrest.CoreMatchers.equalTo;
import static praktikum.service.UserService.randomUser;

public class UserTest extends BaseTest{
    UserService userService = new UserService();
    UserRequest existingUser = new UserRequest("jimmy.berge@yahoo.com",
            "c531d4d72ab23484254ea24aea649c1a50e1bc6c",
            "jasper.rempel");

    @Test
    public void testCreateUserSuccess() {
        var user = randomUser();
        Response response = userService.registerUser(user);

        response
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .statusCode(HttpStatus.SC_OK);

        userService.delete(user);
    }

    @Test
    public void testCreateUserFailOnUserExists() {
        var user = existingUser;
        Response response = userService.registerUser(user);

        response
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"))
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void testCreateUserFailOnMissingPassword() {
        var user = randomUser();
        user.setPassword(null);
        Response response = userService.registerUser(user);

        response
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }


}