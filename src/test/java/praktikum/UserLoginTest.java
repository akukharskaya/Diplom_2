package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static praktikum.service.UserService.randomUser;

public class UserLoginTest extends BaseTest {


    @Test
    @DisplayName("Авторизация существующего пользователя")
    public void testAuthUserSuccess() {
        Response response = userService.auth(existingUser);

        response
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .body("accessToken", CoreMatchers.notNullValue())
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Авторизация несуществующего пользователя")
    public void testAuthUserFailOnNotExistingUser() {
        var user = randomUser();
        Response response = userService.auth(user);

        response
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"))
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }


}
