package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static praktikum.service.UserService.randomUser;

public class UserRegistrationTest extends BaseTest{

    @Test
    @DisplayName("Создание уникального пользователя")
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
    @DisplayName("Регистрация авторизованного пользователя")
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
    @DisplayName("Регистрация пользователя с пустым паролем")
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