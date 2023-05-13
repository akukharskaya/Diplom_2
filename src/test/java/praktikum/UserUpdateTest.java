package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static praktikum.service.UserService.randomUser;

public class UserUpdateTest extends BaseTest{
    @Test
    @DisplayName("Изменение данных авторизованного пользователя")
    public void testUpdateWithAuth(){

        var user = randomUser();
        userService.registerUser(user);
        String token = userService.getToken(user);

        UserRequest expected = UserRequest.builder()
                .email(randomUser().getEmail())
                .name(randomUser().getName())
                .build();

        Response response = userService.updateUser(token,expected);

        response.then()
                .assertThat()
                .body("success", equalTo(true))
                .statusCode(HttpStatus.SC_OK);

        Response info = userService.getInfo(token);
        info.then()
                .assertThat()
                .body("user.email",equalTo(expected.getEmail()))
                .body("user.name",equalTo(expected.getName()))
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Изменение данных неавторизованного пользователя")
    public void testUpdateWithoutAuth() {
        String token = " ";

        UserRequest expected = UserRequest.builder()
                .email(randomUser().getEmail())
                .name(randomUser().getName())
                .build();

        Response response = userService.updateUser(token,expected);

        response.then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message",equalTo("You should be authorised"))
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}
